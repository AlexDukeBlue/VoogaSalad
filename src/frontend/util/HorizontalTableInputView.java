package frontend.util;

import java.util.ArrayList;
import java.util.Collection;

import frontend.BaseUIManager;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

public class HorizontalTableInputView extends TableInputView {
	
	public HorizontalTableInputView(){
		this(new ArrayList<BaseUIManager<Parent>>());
	}

	public HorizontalTableInputView(Collection<BaseUIManager<Parent>> childrenToAdd){
		HBox hbox = new HBox();
		setContent(hbox);
		getChildren().addListener(new ListChangeListener<BaseUIManager<Parent>>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends BaseUIManager<Parent>> change) {
				if (change.wasAdded()){
					change.getAddedSubList().stream().forEach(child -> hbox.getChildren().add(child.getObject()));
				} else if (change.wasRemoved()){
					change.getAddedSubList().stream().forEach(child -> hbox.getChildren().remove(child.getObject()));
				}
			}
		});
	}

}
