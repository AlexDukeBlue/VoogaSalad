package frontend.factory.wizard.strategies.wizard_pages;

import java.util.Collection;
import java.util.stream.Collectors;

import backend.unit.properties.InteractionModifier;
import backend.util.GameplayState;
import controller.Controller;
import frontend.factory.wizard.strategies.wizard_pages.util.IntegerInputRow;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AttackPage extends BaseWizardPage {

	private VBox vbox;
	private PassiveAbilitiesAdderPage passiveAbilityPage;
	private IntegerInputRow numHits;
	private TextField damageField;

	public AttackPage(Controller controller, String descriptionKey) {
		super(controller, descriptionKey);
		initialize();
	}

	@Override
	public Region getNode() {
		return vbox;
	}

	private void initialize() {
		vbox = new VBox();
		passiveAbilityPage = new PassiveAbilitiesAdderPage(getController(), "AttackPassiveAbilitiesAdderPage",
				GameplayState.OFFENSIVE_MODIFIER);
		numHits = new IntegerInputRow(null, getPolyglot().get("NumHits"), new StringBinding() {
			@Override
			protected String computeValue() {
				return "";
			}
		});
		vbox.getChildren().add(numHits.getNode());
		HBox damageHBox = new HBox();
		damageField = new TextField();
		Label damageLabel = new Label();
		damageLabel.textProperty().bind(getPolyglot().get("DamagePerHit"));
		damageHBox.getChildren().addAll(damageLabel, damageField);
		vbox.getChildren().add(damageHBox);
		vbox.getChildren().add(passiveAbilityPage.getNode());
		canNextWritable().setValue(true);
	}

	public double getDamage() {
		try {
			return Double.parseDouble(damageField.getText());
		} catch (Exception e) {
			return 0.0;
		}
	}

	public int getNumHits() {
		return numHits.getValue();
	}

	public Collection<InteractionModifier<Double>> getModifiers() {
		return passiveAbilityPage.getPassiveAbilitiesByCategory(GameplayState.OFFENSIVE_MODIFIER).stream()
				.map(ability -> (InteractionModifier<Double>) ability).collect(Collectors.toList());
	}

}
