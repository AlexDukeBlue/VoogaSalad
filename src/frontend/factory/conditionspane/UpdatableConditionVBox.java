package frontend.factory.conditionspane;

import java.util.Collection;
import java.util.function.Consumer;
import javafx.scene.layout.VBox;

/**
 * An UpdatableConditionVBox displays ConditionBox objects in a VBox and updates
 * them. Combines the dynamic functionality of a Consumer and the visual
 * capabilities of a VBox. This provides a way to maintain various visual lists
 * that differ in their update logic.
 * 
 * @author Stone Mathers Created 4/29/2017
 */
public class UpdatableConditionVBox {

	private Consumer<Collection<ConditionBox>> updateOperation;
	private Collection<ConditionBox> conditionBoxes;
	private VBox myBox = new VBox();

	/**
	 * Constructs an UpdatableConditionVBox given the passed parameters.
	 * 
	 * @param conditionBoxes
	 *            Collection of default ConditionBox objects that will populate
	 *            the UpdatableConditionVBox at instantiation.
	 * @param updateOp
	 *            Consumer that operates upon the held ConditionBox objects when
	 *            updated.
	 */
	public UpdatableConditionVBox(Collection<ConditionBox> conditionBoxes,
			Consumer<Collection<ConditionBox>> updateOp) {
		updateOperation = updateOp;
		this.conditionBoxes = conditionBoxes;
		fillVBox();
	}

	/**
	 * Updates the held ConditionBoxes using the Consumer passed at instantiation.
	 */
	public void update() {
		updateOperation.accept(conditionBoxes);
		fillVBox();
	}

	/**
	 * Return the VBox holding ConditionBox objects.
	 * 
	 * @return VBox
	 */
	public VBox getVBox() {
		return myBox;
	}

	private void fillVBox() {
		myBox.getChildren().clear();
		conditionBoxes.forEach(box -> myBox.getChildren().add(box.getNode()));
	}
}
