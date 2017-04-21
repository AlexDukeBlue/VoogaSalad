/**
 * 
 */
package frontend.factory.conditionspane;

import backend.game_engine.ResultQuadPredicate.Result;
import controller.Controller;
import frontend.util.BaseUIManager;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * @author Stone Mathers
 * Created 4/20/2017
 */
public class ResultantBox extends BaseUIManager<Region> {

	private String myName;
	private HBox myBox = new HBox();
	private VBox toggleBox = new VBox();
	private ToggleGroup toggleGroup = new ToggleGroup();
	
	/**
	 * @param resultantName
	 * @param controller
	 */
	public ResultantBox(String resultantName, Controller controller){
		super(controller);
		myName = resultantName;
		initBox();
	}
	
	@Override
	public Region getObject() {
		return myBox;
	}
	
	private void initBox(){
		initToggleBox();
		myBox.getChildren().addAll(toggleBox, new Text(myName));
	}
	
	private void initToggleBox(){
		RadioButton win = new RadioButton("Win");		//TODO resource file OR image
		win.setToggleGroup(toggleGroup);
		//win.selectedProperty().addListener((o, oV, nV) -> getController().setResultantByName(myName, Result.WIN));	//TODO
		
		RadioButton tie = new RadioButton("Tie");		//TODO resource file OR image
		tie.setToggleGroup(toggleGroup);
		//tie.selectedProperty().addListener((o, oV, nV) -> getController().setResultantByName(myName, Result.TIE));	//TODO 
		
		RadioButton lose = new RadioButton("Lose");	//TODO resource file OR image
		lose.setToggleGroup(toggleGroup);
		//lose.selectedProperty().addListener((o, oV, nV) -> getController().setResultantByName(myName, Result.LOSE));	//TODO 
		
		RadioButton none = new RadioButton("None");	//TODO resource file OR image
		none.setSelected(true);
		none.setToggleGroup(toggleGroup);
		//none.selectedProperty().addListener((o, oV, nV) -> getController().setResultantByName(myName, Result.NONE));	//TODO 
		
		toggleBox.getChildren().addAll(win, tie, lose, none);
	}
	
}
