package frontend.factory.conditionspane;

import java.util.Collection;
import java.util.function.Consumer;
import javafx.scene.layout.VBox;

/**
 * 
 * 
 * @author Stone Mathers
 * Created 4/29/2017
 */
public class UpdatableConditionVBox {

	
	private Consumer<Collection<ConditionBox>> updateOperation;
	private Collection<ConditionBox> conditionBoxes;
	private VBox myBox = new VBox();
	
	/**
	 * Constructs 
	 * 
	 * @param conditionBoxes
	 * @param updateOp
	 */
	public UpdatableConditionVBox(Collection<ConditionBox> conditionBoxes, Consumer<Collection<ConditionBox>> updateOp) {
		updateOperation = updateOp;
		this.conditionBoxes = conditionBoxes;
		fillVBox();
	}
	
	public void update(){
		updateOperation.accept(conditionBoxes);
		fillVBox();
	}

	public VBox getVBox(){
		return myBox;
	}
	
	private void fillVBox(){
		myBox.getChildren().clear();
		conditionBoxes.forEach(box -> myBox.getChildren().add(box.getNode()));
	}
}
