package frontend.factory.wizard.wizards.strategies;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Parent;
import util.polyglot_extended.ObservablePolyglot;

/**
 * WizardStrategy is an interface to be used in the Strategy design pattern. It
 * creates pages to prompt the user for information required to instantiate the
 * object T. The idea is that other classes can switch out the
 * WizardSelectionStrategies so that a single class can be used to instantiate
 * many different types of objects, simply by switching the strategy.
 * 
 * @author Dylan Peters
 *
 * @param <T>
 *            Type of Object that this WizardStrategy is being used to modify or
 *            instantiate.
 */
public interface WizardStrategy<T> {

	/**
	 * Returns whether the strategy can go to the previous page and modify
	 * settings.
	 * 
	 * @return ReadOnlyBooleanProperty boolean property describing whether to
	 *         let the user go to a previous page.
	 */
	ReadOnlyBooleanProperty canPrevious();

	/**
	 * Returns whether the strategy can go to the next page and modify settings.
	 * 
	 * @return ReadOnlyBooleanProperty boolean property describing whether to
	 *         let the user go to a next page.
	 */
	ReadOnlyBooleanProperty canNext();

	/**
	 * Returns whether the strategy has all the information needed to
	 * instantiate an object.
	 * 
	 * @return ReadOnlyBooleanProperty boolean property describing whether the
	 *         strategy has all the information needed to instantiate an object.
	 */
	ReadOnlyBooleanProperty canFinish();

	/**
	 * Returns the object that displays to the user to allow the user to input
	 * settings.
	 * 
	 * @return the object that displays to the user to allow the user to input
	 *         settings.
	 */
	Parent getNode();

	/**
	 * Called to make the strategy go to the previous page to allow the user to
	 * change settings. If the canPrevious method returns false, this method
	 * will not do anything.
	 */
	void previous();

	/**
	 * Called to make the strategy go to the next page to allow the user to
	 * change settings. If the canPrevious method returns false, this method
	 * will not do anything.
	 */
	void next();

	/**
	 * Returns object of type T that has been successfully instantiated by this
	 * strategy. If the canFinish method is returning false, this will return
	 * null because it does not have enough information from the user to
	 * instantiate the object of type T.
	 * 
	 * @return object of type T that has been successfully instantiated by this
	 *         strategy. If the canFinish method is returning false, this will
	 *         return null because it does not have enough information from the
	 *         user to instantiate the object of type T.
	 */
	T finish();
	
	ObservablePolyglot getPolyglot();
	
//	StringBinding getTitle();

}
