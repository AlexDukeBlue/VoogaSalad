package frontend.factory.wizard.strategies;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.layout.Region;
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
	 * Returns a boolean property that tells other classes whether the strategy
	 * requests the wizard to cancel.
	 * 
	 * @return true if the wizard should cancel
	 */
	ReadOnlyBooleanProperty requestsCancel();

	/**
	 * Returns the object that displays to the user to allow the user to input
	 * settings.
	 * 
	 * @return the object that displays to the user to allow the user to input
	 *         settings.
	 */
	Region getNode();

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

	/**
	 * Returns the ObservablePolyglot that the WizardStrategy uses to translate
	 * its text. This allows other classes to listen to changes in the polyglot
	 * or change the wizard's language.
	 * 
	 * @return the ObservablePolyglot that the WizardStrategy uses to translate
	 *         its text
	 */
	ObservablePolyglot getPolyglot();

	/**
	 * Returns the title of the WizardStrategy in the form of a StringBinding,
	 * so that the Wizard can change the text of the string, and anything that
	 * uses it will automatically be updated.
	 * 
	 * @return the title of the WizardStrategy
	 */
	StringBinding getTitle();

}
