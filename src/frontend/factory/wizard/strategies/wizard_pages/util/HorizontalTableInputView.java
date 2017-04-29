package frontend.factory.wizard.strategies.wizard_pages.util;

import java.util.ArrayList;
import java.util.Collection;

import frontend.util.BaseUIManager;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

/**
 * a horizontal implementation of the TableInputView
 * 
 * @author Andreas
 *
 */
public class HorizontalTableInputView extends TableInputView {

	/**
	 * Creates a new instance of HorizontalTableInputView with no children.
	 */
	public HorizontalTableInputView() {
		this(new ArrayList<BaseUIManager<Parent>>());
	}

	/**
	 * Creates an instance of HorizontalTableInputView with the children
	 * specified.
	 * 
	 * @param childrenToAdd
	 *            a collection of BaseUIManagers to add to the
	 *            HorizontalTableInputView
	 */
	public HorizontalTableInputView(Collection<BaseUIManager<Parent>> childrenToAdd) {
		super();
		HBox hbox = new HBox();
		setContent(hbox);
		getChildren().addListener(new ListChangeListener<BaseUIManager<? extends Parent>>() {
			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends BaseUIManager<? extends Parent>> change) {
				if (change.next()) {
					if (change.wasAdded()) {
						change.getAddedSubList().stream().forEach(child -> hbox.getChildren().add(child.getNode()));
					} else if (change.wasRemoved()) {
						change.getAddedSubList().stream().forEach(child -> hbox.getChildren().remove(child.getNode()));
					}
				}
			}
		});
		getChildren().addAll(childrenToAdd);
	}

}
