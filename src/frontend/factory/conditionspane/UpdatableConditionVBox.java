/**
 * 
 */
package frontend.factory.conditionspane;

import java.util.Collection;
import java.util.function.Consumer;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * @author Stone Mathers
 * Created 4/29/2017
 */
public class UpdatableConditionVBox {

	
	private Consumer<Collection<ConditionBox>> updateOperation;
	private Collection<ConditionBox> conditionBoxes;
	private VBox myBox = new VBox();
	
	/**
	 * 
	 */
	public UpdatableConditionVBox(Collection<ConditionBox> conditionBoxes, Consumer<Collection<ConditionBox>> updateOp) {
		updateOperation = updateOp;
		this.conditionBoxes = conditionBoxes;
		initVBox();
	}
	
	public void update(){
		updateOperation.accept(conditionBoxes);
		initVBox();
	}

	public VBox getVBox(){
		return myBox;
	}
	
	private void initVBox(){
		myBox.getChildren().clear();
		conditionBoxes.forEach(box -> myBox.getChildren().add(box.getNode()));
	}
}
