package frontend.factory.conditionspane;

import java.util.Collection;
import java.util.function.Consumer;

import frontend.util.BaseUIManager;
import javafx.scene.layout.VBox;

/**
 * An UpdatableVBox displays objects that extends BaseUIManager in a VBox and
 * updates them. Combines the dynamic functionality of a Consumer and the visual
 * capabilities of a VBox. This provides a way to maintain various visual lists
 * that differ in their update logic.
 * <p>
 * <T> must extend BaseUIManager so that a Node can be retrieved to be
 * displayed. Making this generic allows its use by an subclasses of
 * BaseUIManager that one may want to hold in a visual list capable of being
 * updated.
 * 
 * @author Stone Mathers Created 4/29/2017
 */
public class UpdatableVBox<T extends BaseUIManager<?>> {

	private Consumer<Collection<T>> updateOperation;
	private Collection<T> tObjects;
	private VBox myBox = new VBox();

	/**
	 * Constructs an UpdatableVBox given the passed parameters.
	 * 
	 * @param tObjects
	 *            Collection of default T objects that will populate the
	 *            UpdatableVBox at instantiation.
	 * @param updateOp
	 *            Consumer that operates upon the held T objects when updated.
	 */
	public UpdatableVBox(Collection<T> tObjects, Consumer<Collection<T>> updateOp) {
		updateOperation = updateOp;
		this.tObjects = tObjects;
		fillVBox();
	}

	/**
	 * Updates the held T objects using the Consumer passed at instantiation.
	 */
	public void update() {
		updateOperation.accept(tObjects);
		fillVBox();
	}

	/**
	 * Return the VBox holding T objects.
	 * 
	 * @return VBox
	 */
	public VBox getVBox() {
		return myBox;
	}

	private void fillVBox() {
		myBox.getChildren().clear();
		tObjects.forEach(box -> myBox.getChildren().add(box.getNode()));
	}
}
