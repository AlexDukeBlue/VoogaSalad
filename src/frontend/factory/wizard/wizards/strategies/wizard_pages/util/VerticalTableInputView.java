package frontend.factory.wizard.wizards.strategies.wizard_pages.util;

import java.util.ArrayList;
import java.util.Collection;

import frontend.util.BaseUIManager;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * a vertical implementation of the TableInputView
 * @author Andreas
 *
 */
public class VerticalTableInputView extends TableInputView {

	public VerticalTableInputView() {
		this(new ArrayList<BaseUIManager<Parent>>());
	}

	public VerticalTableInputView(Collection<BaseUIManager<Parent>> childrenToAdd) {
		super();
		VBox vbox = new VBox();
		setContent(vbox);
		getChildren().addListener(new ListChangeListener<BaseUIManager<? extends Parent>>() {
			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends BaseUIManager<? extends Parent>> change) {
				if (change.next()) {
					if (change.wasAdded()) {
						change.getAddedSubList().stream().forEach(child -> vbox.getChildren().add(child.getNode()));
					} else if (change.wasRemoved()) {
						change.getAddedSubList().stream()
								.forEach(child -> vbox.getChildren().remove(child.getNode()));
					}
				}
			}
		});
		getChildren().addAll(childrenToAdd);
	}

}
