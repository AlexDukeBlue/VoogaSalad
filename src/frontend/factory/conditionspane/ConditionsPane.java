/**
 *
 */
package frontend.factory.conditionspane;

import backend.game_engine.Resultant;
import backend.game_rules.GameRule;
import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ComponentClickHandler;
import frontend.factory.wizard.WizardFactory;
import frontend.interfaces.conditionspane.ConditionsPaneExternal;
import frontend.interfaces.conditionspane.ConditionsPaneObserver;
import frontend.util.AddRemoveButton;
import frontend.util.BaseUIManager;
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
public class ConditionsPane extends BaseUIManager<Region> implements ConditionsPaneExternal {

	private VBox myBox = new VBox();
	private Collection<ConditionsPaneObserver> observers;
	private Collection<Resultant> resultants;
	private Collection<GameRule> rules;

	/**
	 *
	 */
	public ConditionsPane(Controller controller, ComponentClickHandler clickHandler) {
		super(controller, clickHandler);
		observers = new ArrayList<>();
		resultants = new ArrayList<>();    //temporary
		rules = new ArrayList<>();            //temporary
		//resultant = getController().getResultants();	//TODO
		//rules = getController().getRules();			//TODO
		initPane();
	}

	@Override
	public Region getObject() {
		return myBox;
	}

	@Override
	public void update() {
		//TODO
		//updateRulePane();
		//updateResultantPane();
	}

	private void initPane() {
		Stream.of("Rules", " End conditions").map(this::createPane).forEach(myBox.getChildren()::add);
	}

	private TitledPane createPane(String type) {
		TitledPane rulesPane = new TitledPane();
		rulesPane.setText(type); //TODO resource file
		rulesPane.setCollapsible(true);

		VBox content = new VBox();
		for (GameRule rule : rules) {
			//content.getChildren().add(new RuleBox(rule.getNameOf()));	//TODO
		}
		content.setAlignment(Pos.TOP_RIGHT);
		AddRemoveButton addRemoveButton = new AddRemoveButton();
		addRemoveButton.setOnAddClicked(e -> WizardFactory.newWizard(type, getController().getAuthoringGameState()).addObserver((o, arg) -> getController().addTemplatesByCategory(type, (VoogaEntity) arg)));
		content.getChildren().add(addRemoveButton.getObject());

		rulesPane.setContent(new ScrollPane(content));
		return rulesPane;
	}
}
