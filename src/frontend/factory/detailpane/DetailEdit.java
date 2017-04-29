package frontend.factory.detailpane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import backend.cell.Terrain;
import backend.grid.CoordinateTuple;
import backend.grid.GridPattern;
import backend.unit.ModifiableUnit;
import backend.unit.Unit;
import backend.unit.properties.ModifiableUnitStat;
import backend.unit.properties.UnitStat;
import backend.util.AuthoringGameState;
import backend.util.ModifiableVoogaCollection;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Faith Rodriguez
 *         <p>
 *         This class allows the user to edit details and characteristics of a unit existing on the board.
 *         <p>
 *         This class is dependent on DetailPane to work effectively.
 */

public class DetailEdit extends BaseUIManager<Node> {

	private Pane pane;
	private Unit unit;
	private GridPane sceneView;
	private Map<Terrain, TextField> moveCosts;
	private Map<String, List<TextField>> unitStats;
	private ComboBox<String> movePatternBox;
	private ComboBox<String> playerNameBox;
	private Stage myStage;
	private int rows = 0;

	public DetailEdit(VoogaEntity sprite, String spriteType, Controller controller) {
		super(controller);
		myStage = new Stage();
		pane = new Pane();
		unit = (Unit) sprite;
		createUnitScene();
		Scene edittingScene = new Scene(pane);
		myStage.setScene(edittingScene);
		myStage.show();
	}

	private void createUnitScene() {
		sceneView = new GridPane();
		Label label = addText("Move Costs");
		sceneView.addRow(rows++, label);
		Collection<Terrain> terrains = (Collection<Terrain>) getController().getAuthoringGameState().getTemplateByCategory("terrain").getAll();
		moveCosts = new HashMap<>();
		for (Terrain t : terrains) {
			TextField feature = createUserInput(t.getFormattedName(), ((Integer) unit.getMoveCostByTerrain(t)).toString()).get(0);
			moveCosts.put(t, feature);
		}
		sceneView.addRow(rows++, new Label("Stats"));
		unitStats = new HashMap<>();
		for (UnitStat stat : unit.getUnitStats()) {
			unitStats.put(stat.getName(), createUserInput(stat.getName(), stat.getMinValue(), stat.getCurrentValue(), stat.getMaxValue()));
		}
		ObservableList<String> options = FXCollections.observableList(getController()
				.getAuthoringGameState()
				.getTemplateByCategory("Grid Pattern").getAll().stream()
				.map(GridPattern.class::cast)
				.filter(e -> e.getShape().equals(getController().getShape()))
				.map(VoogaEntity::getName)
				.collect(Collectors.toList()));
		movePatternBox = createDropDown("Move Pattern", options, unit.getMovePattern().getName());
		playerNameBox = createDropDown("Player", FXCollections.observableList(getController().getReadOnlyGameState().getOrderedPlayerNames()), unit.getOwner().isPresent() ? unit.getOwner().get().getName() : "");
		pane.getChildren().add(sceneView);
		createUnitSubmitBtn();
	}

	private List<TextField> createUserInput(String feature, Object... values) {
		Label label = addText(feature);
		List<Node> featureInfo = new ArrayList<>();
		featureInfo.add(label);
		List<TextField> featureValues = Arrays.stream(values).map(e -> new TextField(e.toString())).collect(Collectors.toList());
		featureInfo.addAll(featureValues);
		sceneView.addRow(rows++, featureInfo.toArray(new Node[0]));
		return featureValues;
	}

	private ComboBox<String> createDropDown(String feature, ObservableList<String> choices, String defaultValue) {
		Label label = addText(feature);
		ComboBox<String> options = new ComboBox<>(choices);
		options.setValue(defaultValue);
		sceneView.addRow(rows++, label, options);
		return options;
	}

	private Label addText(String feature) {
		Label label = new Label();
		label.setText(feature + ": ");
		return label;
	}

	@SuppressWarnings("unchecked")
	private void createUnitSubmitBtn() {
		Button submit = new Button("Submit Changes");
		sceneView.addRow(rows++, submit);
		Map<Terrain, Integer> finalCosts = new HashMap<>();
		submit.setOnAction(e -> {
			for (Terrain key : moveCosts.keySet()) {
				finalCosts.put(key, Integer.parseInt(moveCosts.get(key).getText()));
			}
			myStage.close();

			Map<String, List<? extends Number>> stats = unitStats.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
							.map(field -> {
								try {
									return Integer.parseInt(field.getText());
								} catch (NumberFormatException nfe) {
									return Double.parseDouble(field.getText());
								}
							}).collect(Collectors.toList())));

			CoordinateTuple unitLocation = unit.getLocation();
			String unitName = unit.getName();

			String movePatternName = movePatternBox.getValue();
			String playerName = playerNameBox.getValue();
			getController().sendModifier((AuthoringGameState state) -> {
//				ModifiableUnit newUnit = (ModifiableUnit) getTemplateByCategory("Unit").getAll().stream()
//						.filter(x -> x.getName().equals(unitName));
				ModifiableUnit newUnit = (ModifiableUnit) state.getGrid().get(unitLocation).getOccupantByName(unitName);
				newUnit.setTerrainMoveCosts(finalCosts);
				stats.forEach((names, values) -> ((ModifiableUnitStat) newUnit
						.getUnitStat(names))
						.setMinValue(values.get(0))
						.setCurrentValue(values.get(1))
						.setMaxValue(values.get(2)));
				newUnit.setMovePattern((GridPattern) state
						.getTemplateByCategory("Grid Pattern")
						.getByName(movePatternName));
				newUnit.setOwner(state.getPlayerByName(playerName));
				return state;
			});

		});
	}

	private ModifiableVoogaCollection<VoogaEntity, ? extends ModifiableVoogaCollection> getTemplateByCategory(
			String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getNode() {
		return pane;
	}
}
