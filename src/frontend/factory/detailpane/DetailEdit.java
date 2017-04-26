package frontend.factory.detailpane;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.python.google.common.collect.Iterables;

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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DetailEdit extends BaseUIManager {

	Pane pane;
	Unit unit;
	Terrain terrain;
	VBox sceneView;
	Map<Terrain, TextField> moveCosts;
	TextField[] unitStats;
	String movePattern;
	Stage myStage;
	
	public DetailEdit(VoogaEntity sprite, String spriteType, Controller controller) {
		super(controller);
		myStage = new Stage();
		pane = new Pane();
		if (spriteType.equals("Unit")) {
			unit = (Unit) sprite;
			createUnitScene();
		}
		else {
			//createTerrainScene();
		}
		Scene edittingScene = new Scene(pane);
		myStage.setScene(edittingScene);
		myStage.show();
	}
	
	@SuppressWarnings("rawtypes")
	private void createUnitScene() {
		sceneView = new VBox();
		Label label = addText("Move Costs");
		sceneView.getChildren().add(label);
		Collection<Terrain> terrains = (Collection<Terrain>) getController().getAuthoringGameState().getTemplateByCategory("terrain").getAll();
		moveCosts = new HashMap<Terrain, TextField>();
		for (Terrain t : terrains) {
			TextField feature = createUserInput(t.getFormattedName(), ((Integer) unit.getMoveCostByTerrain(t)).toString());
			moveCosts.put(t, feature);
		}
		unitStats = new TextField[unit.getUnitStats().size()];
		
		Collection<? extends UnitStat> stats = unit.getUnitStats(); 
		for (int i=0; i<stats.size(); i++) {
			UnitStat stat = Iterables.get(stats, i);
			unitStats[i] = createUserInput(stat.getName(),(stat).getCurrentValue().toString());
			
		}
		
		
		ObservableList<String> options =  FXCollections.observableArrayList(getController()
				.getAuthoringGameState()
				.getTemplateByCategory("Grid Pattern").getAll().stream()
				.map(GridPattern.class::cast)
				.filter(e -> e.getShape().equals(getController().getShape()))
				.map(VoogaEntity::getName)
				.collect(Collectors.toList()));
			 
		createDropDown("Move Pattern", options);
		pane.getChildren().add(sceneView);
		createUnitSubmitBtn();
		
	}
	
	private TextField createUserInput(String feature, String value) {
		HBox featureInfo = new HBox();
		featureInfo.setPadding(new Insets(5,5,5,5));
		Label label = addText(feature);
		TextField featureValue = new TextField(value);
		featureInfo.getChildren().addAll(label, featureValue);
		sceneView.getChildren().add(featureInfo);
		return featureValue;
	}
	
	@SuppressWarnings("unchecked")
	private ComboBox createDropDown(String feature, ObservableList<String> choices) {
		HBox featureInfo = new HBox();
		featureInfo.setPadding(new Insets(5,5,5,5
				));
		Label label = addText(feature);
		ComboBox options = new ComboBox(choices);
		options.getSelectionModel().selectedItemProperty()
	    .addListener(new ChangeListener<String>() {
	        
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				movePattern = newValue;
			}
	});
		featureInfo.getChildren().addAll(label, options);
		sceneView.getChildren().add(featureInfo);
		return options;
	}
	
	private Label addText(String feature) {
		Label label = new Label();
		label.setText(feature + ": ");
		return label;
	}

	private void createUnitSubmitBtn() {
		Button submit = new Button("Submit Changes");
		sceneView.getChildren().add(submit);
		Map<Terrain, Integer> finalCosts = new HashMap();
		submit.setOnAction(e -> {
			for(Terrain key: moveCosts.keySet()) {
				finalCosts.put(key, Integer.parseInt(moveCosts.get(key).getText()));
			}
//			modifiable.setTerrainMoveCosts(finalCosts);
//			((ModifiableUnitStat) modifiable.getHitPoints()).setMaxValue(Double.parseDouble(unitStats[0].getText()));
//			((ModifiableUnitStat) modifiable.getMovePoints()).setMaxValue(Integer.parseInt(unitStats[1].getText()));
//			if (movePattern!= null) {
//				modifiable.setMovePattern((GridPattern) getController()
//					.getAuthoringGameState()
//					.getTemplateByCategory("Grid Pattern")
//					.getByName(movePattern));
				myStage.close();
			//}
			CoordinateTuple unitLocation = unit.getLocation();
			String unitName = unit.getName();
			System.out.println(unitStats[0].getText());
			double maxAbility = Double.parseDouble(unitStats[0].getText());
			double maxHit = Double.parseDouble(unitStats[1].getText());
			int maxMove = Integer.parseInt(unitStats[2].getText());
			String moveP = movePattern;
			
			getController().sendModifier((AuthoringGameState state) -> {
				ModifiableUnit newUnit = (ModifiableUnit) state.getGrid().get(unitLocation).getOccupantByName(unitName);
				newUnit.setTerrainMoveCosts(finalCosts);
				((ModifiableUnitStat) newUnit.getHitPoints()).setMaxValue(maxHit);
				((ModifiableUnitStat) newUnit.getMovePoints()).setMaxValue(maxMove);
				((ModifiableUnitStat) newUnit.getAbilityPoints()).setMaxValue(maxAbility);
				if (moveP!= null) {
					newUnit.setMovePattern((GridPattern) state
						.getTemplateByCategory("Grid Pattern")
						.getByName(moveP));
				}
				return state;
			});
			
		});
	}
	
	@Override
	public Object getObject() {
		return pane;
	}
}
