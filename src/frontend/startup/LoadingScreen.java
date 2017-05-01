package frontend.startup;

import javafx.animation.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoadingScreen extends Group {
	private double minWidth;
	private double minHeight;

	public LoadingScreen(double minWidth, double minHeight) {
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		createCircles();
	}

	public void createCircles() {
		for (int cont = 0; cont < Math.random() * 10; cont++) {
			Circle circle = new Circle();
			circle.setCenterX(Math.random() * minWidth);
			circle.setCenterY(Math.random() * minHeight);
			circle.setFill(Color.color(Math.random(), Math.random(), Math.random()));
			createCircle(circle);
		}
	}


	public Text createText(Circle circle) {
		Text loading = new Text();
		loading.setText("Loading");
		loading.setFont(Font.loadFont("file:resources/styles/PlaylistFF/Playlist Script.otf", 10));
		loading.setFont(Font.font(javafx.scene.text.Font.getFamilies().get((int) (Math.random() * javafx.scene.text.Font.getFamilies().size()))));
		double lsWidth = (loading.getLayoutBounds().getMaxX() - loading.getLayoutBounds().getMinX());
		loading.xProperty().bind(circle.centerXProperty().subtract(lsWidth / 2));

		loading.yProperty().bind(circle.centerYProperty());


		return loading;
	}

	public void createCircle(Circle circle) {
		circle.setFill(Color.WHITE);
		circle.setEffect(new GaussianBlur(Math.random() * 8 + 2));
		circle.setOpacity(Math.random());
		circle.setRadius(20);

		this.getChildren().add(circle);
		circle.setOnMouseDragged(e -> run(circle));

		Text loading = createText(circle);
		this.getChildren().add(loading);


		double randScale = (Math.random() * 4) + 1;
		KeyValue kValueX = new KeyValue(circle.scaleXProperty(), randScale);
		KeyValue kValueY = new KeyValue(circle.scaleYProperty(), randScale);
		KeyValue loadingkValueX = new KeyValue(loading.scaleXProperty(), randScale - 1);
		KeyValue loadingkValueY = new KeyValue(loading.scaleYProperty(), randScale - 1);


		KeyValue color = new KeyValue(circle.fillProperty(), Color.color(Math.random(), Math.random(), Math.random()));
		KeyFrame kFrame = new KeyFrame(Duration.millis(5000 + (Math.random() * 5000)), kValueX, kValueY, color, loadingkValueX, loadingkValueY);

		Timeline linhaT = new Timeline(30);

		linhaT.getKeyFrames().add(kFrame);
		linhaT.setAutoReverse(true);
		linhaT.setCycleCount(Animation.INDEFINITE);
		linhaT.play();
	}

	public void drag(Circle circle, double x, double y) {
		if (circle.contains(new Point2D(x, y))) {
			circle.setCenterX(x);
			circle.setCenterY(y);
		}
	}

	public void run(Circle circle) {
		double maxX = circle.getCenterX() + circle.getBoundsInLocal().getWidth() / 2;
		double minX = circle.getCenterX() - circle.getBoundsInLocal().getWidth() / 2;
		double maxY = circle.getCenterY() + circle.getBoundsInLocal().getHeight() / 2;
		double minY = circle.getCenterY() - circle.getBoundsInLocal().getHeight() / 2;

		if (minX > 10 && maxX < minWidth - 10 && minY > 10 && maxY < minHeight - 10) {
			circle.setOnMouseMoved(e -> drag(circle, e.getX(), e.getY()));
		}

	}

	private void moveCircleOnMousePress(Scene scene, final Circle circle, final TranslateTransition transition) {
		scene.setOnMousePressed(event -> {
			if (!event.isControlDown()) {
				circle.setCenterX(event.getSceneX());
				circle.setCenterY(event.getSceneY());
			} else {
				transition.setToX(event.getSceneX() - circle.getCenterX());
				transition.setToY(event.getSceneY() - circle.getCenterY());
				transition.playFromStart();
			}
		});
	}

}
