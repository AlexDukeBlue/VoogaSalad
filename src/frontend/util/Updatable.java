package frontend.util;

/**
 * Updatable objects should be capable of changing their state or their view
 * when they are updated. One common pattern would be to have a view object that
 * holds a reference to a model object, and the view object can have its
 * update() method called when something in the model changes. This is
 * particularly useful when the model is not observable and cannot be changed to
 * be observable.
 * 
 * This interface is a functional interface because it only has one method.
 * Therefore, it can be written as a lambda expression.
 * 
 * @author dylanpeters
 *
 */
@FunctionalInterface
public interface Updatable {

	/**
	 * Tells the object to update to reflect changes made somewhere else in the
	 * code.
	 */
	void update();

}
