package frontend.wizards.selection_strategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;
import frontend.util.BaseUIManager;
import frontend.wizards.wizard_pages.WizardPage;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class BaseSelectionStrategy<T> extends BaseUIManager<Region> implements WizardSelectionStrategy<T> {

	private BooleanProperty canPrevious, canNext, canFinish;
	private BorderPane borderPane;
	private VBox titleDescriptionBox;
	private Label title;
	private Label description;
	private ResourceBundle bundle = ResourceBundle.getBundle("frontend.wizards.selection_strategies.properties/BaseSelection");
	//, new Locale("es", "MX")
	private ObservableList<WizardPage> pages;

	public BaseSelectionStrategy() {
		this(new ArrayList<>());
	}

	public BaseSelectionStrategy(Collection<WizardPage> pages) {
		initialize(pages);
	}

	@Override
	public ReadOnlyBooleanProperty canPrevious() {
		return BooleanProperty.readOnlyBooleanProperty(canPrevious);
	}

	@Override
	public ReadOnlyBooleanProperty canNext() {
		return BooleanProperty.readOnlyBooleanProperty(canNext);
	}

	@Override
	public ReadOnlyBooleanProperty canFinish() {
		return BooleanProperty.readOnlyBooleanProperty(canFinish);
	}

	@Override
	public Region getObject() {
		return borderPane;
	}

	@Override
	public void previous() {
		tryToGoToPageNum(getCurrentPageNum() - 1);
	}

	@Override
	public void next() {
		tryToGoToPageNum(getCurrentPageNum() + 1);
	}

	protected ObservableList<WizardPage> getPages() {
		return pages;
	}

	protected int getCurrentPageNum() {
		int i = 0;
		for (WizardPage page : pages) {
			if (borderPane.getCenter() == page.getObject()) {
				return i;
			}
			i++;
		}
		return -1;
	}

	protected WizardPage getCurrentPage() {
		return getCurrentPageNum() == -1 ? null : pages.get(getCurrentPageNum());
	}

	private void tryToGoToPageNum(int newPageNum) {
		if (canGoToPage(newPageNum)) {
			WizardPage page = pages.get(newPageNum);
			borderPane.setCenter(page.getObject());
			title.setText(page.getTitle());
			description.setText(page.getDescription());

			canPrevious.unbind();
			canPrevious.setValue(newPageNum != 0);

			canNext.unbind();
			if (newPageNum == pages.size() - 1) {
				canNext.setValue(false);
			} else {
				canNext.bind(page.canNext());
			}
			canFinish.unbind();
			if (newPageNum == pages.size() - 1) {
				canFinish.bind(page.canNext());
			} else {
				canFinish.setValue(false);
			}
		} else if (!pages.isEmpty()) {
			tryToGoToPageNum(0);
		}
	}

	private boolean canGoToPage(int newPageNum) {
		return newPageNum >= 0 && newPageNum < pages.size();
	}

	private void initialize(Collection<WizardPage> pages) {
		canPrevious = new SimpleBooleanProperty(false);
		canNext = new SimpleBooleanProperty(false);
		canFinish = new SimpleBooleanProperty(false);
		borderPane = new BorderPane();
		title = new Label();
		description = new Label();
		titleDescriptionBox = new VBox();
		titleDescriptionBox.getChildren().addAll(title, description);
		titleDescriptionBox.setAlignment(Pos.CENTER);
		borderPane.setTop(titleDescriptionBox);
		this.pages = FXCollections.observableArrayList();
		this.pages.addListener(new ListChangeListener<WizardPage>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends WizardPage> c) {
				if (getCurrentPageNum() == -1) {
					tryToGoToPageNum(0);
				}
			}
		});
		this.pages.addAll(pages);
	}
	
	protected ResourceBundle getBundle(){
		return bundle;
	}
	protected String getString(String string){
		return bundle.getString(string);
	}
}

