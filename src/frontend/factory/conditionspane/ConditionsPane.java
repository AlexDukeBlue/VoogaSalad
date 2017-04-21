/**
 * 
 */
package frontend.factory.conditionspane;

import java.util.ArrayList;
import java.util.Collection;

import backend.game_engine.Resultant;
import backend.game_rules.GameRule;
import controller.Controller;
import frontend.interfaces.conditionspane.ConditionsPaneExternal;
import frontend.interfaces.conditionspane.ConditionsPaneObserver;
import frontend.util.BaseUIManager;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * @author Stone Mathers
 * Created 4/20/2017
 */
public class ConditionsPane extends BaseUIManager<Region> implements ConditionsPaneExternal {
	
	private VBox myBox = new VBox();
	private Collection<ConditionsPaneObserver> observers;
	private Collection<Resultant> resultants;
	private Collection<GameRule> rules;
	
	/**
	 * 
	 */
	public ConditionsPane(Controller controller) {
		super(controller);
		observers = new ArrayList<ConditionsPaneObserver>();
		resultants = new ArrayList<Resultant>();	//temporary
		rules = new ArrayList<GameRule>();			//temporary
		//resultant = getController().getResultants();	//TODO
		//rules = getController().getRules();			//TODO
		initPane();
	}

	@Override
	public void addConditionsPaneObserver(ConditionsPaneObserver observer) {
		observers.add(observer);
	}

	@Override
	public void addAllConditionsPaneObservers(Collection<ConditionsPaneObserver> observers) {
		observers.addAll(observers);
	}

	@Override
	public void removeConditionsPaneObserver(ConditionsPaneObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void removeAllConditionsPaneObservers(Collection<ConditionsPaneObserver> observers) {
		observers.removeAll(observers);
	}

	@Override
	public Region getObject() {
		return myBox;
	}
	
	@Override
	public void update(){
		//TODO
		//updateRulePane();
		//updateResultantPane();
	}
	
	private void initPane(){
		myBox.getChildren().addAll(createRulePane(), createResultantPane());
	}

	private TitledPane createRulePane() {
		TitledPane rulesPane = new TitledPane();
		rulesPane.setText("Rules"); //TODO resource file
		rulesPane.setCollapsible(true);
		
		VBox content = new VBox();
		for(GameRule rule: rules){
			//content.getChildren().add(new RuleBox(rule.getNameOf()));	//TODO
		}
		
		Button newRule = new Button("Add Rule");    //TODO resource file OR put a "+" image
		newRule.setOnMouseClicked(e -> createRule());
		content.getChildren().add(newRule);
		
		rulesPane.setContent(new ScrollPane(content));
		return rulesPane;
	}
	
	private void createRule(){
		//TODO
		//RulesWizard wiz = new RulesWizard();
		//wiz.addObserver((wizard, rule) -> getController().addRule((GameRule) rule));
	}
	
	private TitledPane createResultantPane() {
		TitledPane resultPane = new TitledPane();
		resultPane.setText("End Conditions"); //TODO resource file
		resultPane.setCollapsible(true);
		
		VBox content = new VBox();
		for(Resultant result: resultants){
			content.getChildren().add((new ResultantBox(result.getNameOf(), getController())).getObject());	
		}
		
		Button newResult = new Button("Add Condition");    //TODO resource file OR put a "+" image
		newResult.setOnMouseClicked(e -> createResultant());
		content.getChildren().add(newResult);
		
		resultPane.setContent(new ScrollPane(content));
		return resultPane;
	}
	
	private void createResultant(){
		//TODO
		//ResultantWizard wiz = new ResultantWizard();
		//wiz.addObserver((wizard, resultant) -> getController().addResultant((Resultant) resultant));
	}

}
