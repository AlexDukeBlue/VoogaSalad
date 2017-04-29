package frontend.util;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The ButtonFactory creates a way to easily generate different, complex types
 * of buttons with different graphical interfaces, without having to directly
 * manipulate the graphical properties of the button. Simply call the method
 * specifying the button type and the factory sets up the properties of the
 * button.
 * 
 * @author Dylan Peters, Noah Pritt
 *
 */
public class ButtonFactory {

	/**
	 * Creates a button that changes color upon pressing and displays a spinning
	 * rectangle to show the user that the request is being processed.
	 * 
	 * @return
	 */
	public static Button newSpinningButton() {
		Button button = new Button();
		new ButtonFactory().styleButton(button);
		return button;
	}

	/**
	 * 
	 * basic animation idea from https://gist.github.com/james-d/8474941, but
	 * heavily refactored and changed by ncp14, dlp22
	 * 
	 * @param button the button to style
	 */
	private void styleButton(Button button) {
		Color startColor = Color.web("#e08090");
		Color endColor = Color.web("#80e090");
		SimpleObjectProperty<Color> color = new SimpleObjectProperty<>(startColor);
		button.styleProperty()
				.bind(Bindings.createStringBinding(
						() -> String.format("-fx-body-color: rgb(%d, %d, %d);", (int) (256 * color.get().getRed()),
								(int) (256 * color.get().getGreen()), (int) (256 * color.get().getBlue())),
						color));
		final Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, new KeyValue(color, startColor)),
				new KeyFrame(javafx.util.Duration.seconds(1), new KeyValue(color, endColor)));
		button.setOnAction(event -> {
			timeline.play();
		});
		// Create a rotating rectangle and set it as the graphic for the
		final Rectangle rotatingRect = new Rectangle(5, 5, 10, 6);
		rotatingRect.setFill(Color.CORNFLOWERBLUE);
		Pane rectHolder = generateShape(rotatingRect);
		RotateTransition rotate = generateRotation(rotatingRect);
		button.setGraphic(rectHolder);
		// make the rectangle rotate when the mouse hovers over the button
		button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> rotate.play());
		button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
			rotate.stop();
			rotatingRect.setRotate(0);
		});
	}

	private Pane generateShape(Rectangle rotatingRect) {
		final Pane rectHolder = new Pane();
		rectHolder.setMinSize(20, 16);
		rectHolder.setPrefSize(20, 16);
		rectHolder.setMaxSize(20, 16);
		rectHolder.getChildren().add(rotatingRect);
		return rectHolder;
	}

	private RotateTransition generateRotation(Rectangle rotatingRect) {
		final RotateTransition rotate = new RotateTransition(javafx.util.Duration.seconds(1), rotatingRect);
		rotate.setByAngle(360);
		rotate.setCycleCount(Animation.INDEFINITE);
		rotate.setInterpolator(Interpolator.LINEAR);
		return rotate;
	}

}
