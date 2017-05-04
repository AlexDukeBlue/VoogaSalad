package frontend.factory.conditionspane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import backend.util.VoogaEntity;
import controller.Controller;
import frontend.ClickHandler;
import frontend.ClickableUIComponent;
import frontend.factory.wizard.WizardFactory;
import frontend.interfaces.conditionspane.ConditionsPaneExternal;
import frontend.util.AddRemoveButton;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Pos;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * A ConditionsPane displays the various rules of the game such as requirements
 * to end a turn, events that happen at the beginning of a turn, or win/loss/tie
 * conditions. It also provides an interface with which the user can activate
 * and deactivate the rules which they have created. Communications to pass
 * requests to the model and visually update the resulting changes are done
 * through a Controller.
 * <p>
 * ConditionsPane extends ClickableUIComponent so that its components can
 * perform actions when clicked.
 * 
 * @author Stone Mathers Created 4/20/2017
 */
public class ConditionsPane extends ClickableUIComponent<Region>implements ConditionsPaneExternal {

	private VBox myBox = new VBox();
	private UpdatableConditionVBoxFactory boxFactory;
	private Collection<UpdatableVBox<ConditionBox>> subBoxes;

	/**
	 * Constructs a ConditionsPane using the passed parameters.
	 * 
	 * @param controller
	 *            Controller used to communicate with the Model and correctly
	 *            update.
	 * @param clickHandler
	 *            ClickHandler used to determine how clicks will be handled.
	 */
	public ConditionsPane(Controller controller, ClickHandler clickHandler) {
		super(controller, clickHandler);
		boxFactory = new UpdatableConditionVBoxFactory(controller, clickHandler);
		subBoxes = new ArrayList<UpdatableVBox<ConditionBox>>();
		initPane();
	}

	@Override
	public Region getNode() {
		return myBox;
	}

	@Override
	public void update() {
		subBoxes.forEach(box -> ((UpdatableVBox<ConditionBox>) box).update());
	}

	private void initPane() {
		Stream.of(getPolyglot().get("TurnRequirements"), getPolyglot().get("TurnActions"),
				getPolyglot().get("EndConditions")).map(this::createPane).forEach(myBox.getChildren()::add);
	}

	private TitledPane createPane(StringBinding type) {
		TitledPane rulesPane = new TitledPane();
		rulesPane.textProperty().bind(type);
		rulesPane.setCollapsible(true);
		UpdatableVBox<ConditionBox> content = boxFactory.createConditionVBox(getPolyglot(), type.getValueSafe());
		content.getVBox().setAlignment(Pos.TOP_RIGHT);
		AddRemoveButton addRemoveButton = new AddRemoveButton(getClickHandler());
		addRemoveButton.setOnAddClicked(e -> WizardFactory
				.newWizard(type.getValueSafe(), getController(), getPolyglot().getLanguage(), getStyleSheet().getValue())
				.addObserver((o, arg) -> getController().addTemplatesByCategory(type.getValueSafe(), (VoogaEntity) arg)));
		subBoxes.add(content);
		VBox outsideContent = new VBox(content.getVBox(), addRemoveButton.getNode());
		rulesPane.setContent(outsideContent);
		return rulesPane;
	}
}
