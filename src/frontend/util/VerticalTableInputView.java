package frontend.util;

import java.util.ArrayList;
import java.util.Collection;

import frontend.BaseUIManager;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class VerticalTableInputView extends TableInputView {
	
	public VerticalTableInputView(){
		this(new ArrayList<BaseUIManager<Parent>>());
	}
	
	public VerticalTableInputView(Collection<BaseUIManager<Parent>> childrenToAdd){
		VBox vbox = new VBox();
		setContent(vbox);
		getChildren().addListener(new ListChangeListener<BaseUIManager<Parent>>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends BaseUIManager<Parent>> change) {
				if (change.wasAdded()){
					change.getAddedSubList().stream().forEach(child -> vbox.getChildren().add(child.getObject()));
				} else if (change.wasRemoved()){
					change.getAddedSubList().stream().forEach(child -> vbox.getChildren().remove(child.getObject()));
				}
			}
		});
	}

}
