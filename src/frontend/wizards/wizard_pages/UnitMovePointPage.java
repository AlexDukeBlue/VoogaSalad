package frontend.wizards.wizard_pages;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

/**
 * The WizardPage for setting the move cost of units across certain types of terrain
 * @author Andreas
 *
 */
public class UnitMovePointPage extends BaseWizardPage {
	private static final String DEFAULT_TITLE = "Set Movement Points";
	private static final String DEFAULT_DESCRIPTION = "Enter the number of movement points it takes for the unit to cross each terrain type.";

	Button button;

	public UnitMovePointPage() {
		this(DEFAULT_TITLE);
	}

	public UnitMovePointPage(String title) {
		this(title, DEFAULT_DESCRIPTION);
	}

	public UnitMovePointPage(String title, String description) {
		super(title, description);
		button = new Button("UnitMovePointPage");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				canNextWritable().setValue(true);
			}
		});
	}

	@Override
	public Region getObject() {
		return button;
	}

}
