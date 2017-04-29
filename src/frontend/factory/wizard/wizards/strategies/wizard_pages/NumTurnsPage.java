package frontend.factory.wizard.wizards.strategies.wizard_pages;

import frontend.factory.wizard.wizards.strategies.wizard_pages.util.NumericInputRow;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.TableInputView;
import frontend.factory.wizard.wizards.strategies.wizard_pages.util.VerticalTableInputView;
import javafx.scene.layout.Region;

public class NumTurnsPage extends BaseWizardPage{
	
	private TableInputView table;
	private NumericInputRow input;
	
	public NumTurnsPage(String descriptionKey){
		super(descriptionKey);
		initialize();
	}

	@Override
	public Region getNode() {
		return table.getNode();
	}
	
	public int getNumTurns(){
		return input.getValue();
	}
	
	private void initialize(){
		input = new NumericInputRow(null, getPolyglot().get("NumTurnsPrompt").getValueSafe(), getPolyglot().get("NumTurnsLabel").getValueSafe());
		input.setValue(Integer.MAX_VALUE);
		table = new VerticalTableInputView();
		table.getChildren().add(input);
		canNextWritable().setValue(true);
	}

}
