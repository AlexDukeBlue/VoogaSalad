/**
 *
 */
package frontend.factory.conditionspane;

import backend.game_engine.Resultant;
import backend.game_rules.GameRule;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickableUIComponent;
import frontend.ClickHandler;
import frontend.factory.wizard.WizardFactory;
import frontend.interfaces.conditionspane.ConditionsPaneExternal;
import frontend.interfaces.conditionspane.ConditionsPaneObserver;
import frontend.util.AddRemoveButton;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Stone Mathers
 *         Created 4/20/2017
 */
public class ConditionsPane extends ClickableUIComponent<Region> implements ConditionsPaneExternal {

	private VBox myBox = new VBox();
	private ConditionVBoxFactory boxFactory;
	private Collection<ConditionsPaneObserver> observers;
	private Collection<Resultant> resultants;
	private Collection<GameRule> rules;

	/**
	 *
	 */
	public ConditionsPane(Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		boxFactory = new ConditionVBoxFactory(controller, clickHandler);
		observers = new ArrayList<>();
		resultants = new ArrayList<>();    //temporary
		rules = new ArrayList<>();            //temporary
		//resultant = getController().getResultants();	//TODO
		//rules = getController().getRules();			//TODO
		initPane();
	}

	@Override
	public Region getNode() {
		return myBox;
	}

	@Override
	public void update() {
		//TODO
		//updateRulePane();
		//updateResultantPane();
	}

	private void initPane() {
		Stream.of("TurnRequirements", "TurnActions", "EndConditions").map(this::createPane).forEach(myBox.getChildren()::add);
	}

	private TitledPane createPane(String type) {
		TitledPane rulesPane = new TitledPane();
		rulesPane.textProperty().bind(getPolyglot().get(type));
		rulesPane.setCollapsible(true);
		VBox content = boxFactory.createConditionVBox(getPolyglot(), type);
		content.setAlignment(Pos.TOP_RIGHT);
		AddRemoveButton addRemoveButton = new AddRemoveButton(getClickHandler());
		addRemoveButton.setOnAddClicked(e -> WizardFactory.newWizard(type, getController().getAuthoringGameState(),getPolyglot().getLanguage(),getStyleSheet().getValue()).addObserver((o, arg) -> getController().addTemplatesByCategory(type, (VoogaEntity) arg)));
		content.getChildren().add(addRemoveButton.getNode());

		rulesPane.setContent(new ScrollPane(content));
		return rulesPane;
	}
}
