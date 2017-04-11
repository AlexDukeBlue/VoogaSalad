package frontend.wizards.new_voogaobject_wizard;

import backend.util.AuthoringGameState;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ModalNewUnitPane extends NewUnitPane {

	private Stage stage;

	public ModalNewUnitPane(AuthoringGameState gameState) {
		super(gameState);
		initialize();
	}

	@Override
	protected void submit() {
		super.submit();
		stage.close();
	}
	
	@Override
	protected void cancel() {
		stage.close();
	}
	
	@Override
	protected void newNewUnitPane(){
		new ModalNewUnitPane(null);
	}

	private void initialize() {
		stage = new Stage();
		stage.setScene(new Scene(getObject()));
		stage.show();
	}

}
