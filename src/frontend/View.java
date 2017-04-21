/**
 * The View is responsible for displaying the data held within the Model. It is also responsible
 * for providing an interface with which the user can interact to change the GameState.
 * View does not do so directly, instead sending modifiers and getting updated through the Controller.
 * <p>
 * The View organizes the GUI, instantiating and placing all of the necessary panes. Requests are made through a
 * Modifier class, a wrapper holding lambda expressions. The panes are expected to extend BaseUIManager, so that
 * View can access getObject(), the pane can send Modifiers, and the pane can update itself. They are all given
 * an instance of Controller to do so, specifically by adding itself as an observer (so that it’s update method
 * is called whenever a change in the GameState occurs) and calling a method from the controller to send Modifiers.
 * <p>
 * The View also has a boolean editable that determines which mode it is in, edit or play. This determines which
 * GUI components are made available to the user. The methods that instantiate the GUI should use this boolean to
 * determine which components are displayed.
 *
 * @author Stone Mathers, Dylan Peters
 * Created 4/3/2017
 */
package frontend;

import backend.util.AuthoringGameState;
import controller.Controller;
import frontend.factory.GameObserverFactory;
import frontend.factory.detailpane.DetailPaneFactory;
import frontend.factory.templatepane.TemplatePaneFactory;
import frontend.factory.worldview.MinimapPane;
import frontend.factory.worldview.WorldViewFactory;
import frontend.interfaces.GameObserver;
import frontend.interfaces.detailpane.DetailPaneExternal;
import frontend.interfaces.templatepane.TemplatePaneExternal;
import frontend.interfaces.worldview.WorldViewExternal;
import frontend.menubar.VoogaMenuBar;
import frontend.util.BaseUIManager;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class View extends BaseUIManager<Region> {
	private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();
	private Stage myStage;

	static {
		IMAGE_CACHE.put("", new Image("resources/images/transparent.png"));
	}

	private boolean editable;
	private SplitPane outerSplitPane;
	private VoogaMenuBar menuBar;
	private WorldViewExternal worldView;
	private DetailPaneExternal detailPane;
	private TemplatePaneExternal tempPane;
	private GameObserver gameObserver;
	// private RulesPane rulesPane; //TODO For when rules pane is created

	public View(Controller controller) {
		this(controller, new Stage(), true);
	}

	public View(Controller controller, Stage stage) {
		this(controller, stage, true);
	}

	public View(Controller controller, Stage stage, boolean editable) {
		super(controller);
		myStage = stage;
		this.editable = editable;
		placePanes();
		setEditable(editable);
		getStyleSheet().setValue(getPossibleStyleSheetNamesAndFileNames().get("Default Theme"));
		update();
	}

	/**
	 * @param editable True if this View can be switched into "edit" mode, false if
	 *                 it cannot.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
		if (editable) {
			enterAuthorMode();
			menuBar.setEditable(true);
		} else {
			enterPlayMode();
			menuBar.setEditable(false);
		}
	}

	public void toggleRulesPane() {
//		if(myBorder.getLeft() == null){
//			//myBorder.setLeft(rulesPane.getObject());				//TODO For when rules pane is created
//		} else {
//			myBorder.setLeft(null);
//		}
	}

	public void toggleTemplatePane() {
		//TODO
//		if(myBorder.getRight() == null){
//			myBorder.setRight(tempPane.getObject());
//		} else {
//			myBorder.setRight(null);
//		}
	}

	public void toggleDetailsPane() {
		//TODO
//		if(myBorder.getBottom() == null){
//			myBorder.setBottom(detailPane.getObject());
//		} else {
//			myBorder.setBottom(null);
//		}
	}

	public void toggleStatsPane() {
		//TODO
	}

	@Override
	public Region getObject() {
		return outerSplitPane;
	}

	/**
	 * @return The Stage that the View is being displayed on
	 */
	public Stage getStage() {
		return myStage;
	}

	/**
	 * Sets the GameState that the View accesses its data from.
	 *
	 * @param newGameState AuthoringGameState that the View will now access its data from
	 */
	public void setGameState(AuthoringGameState newGameState) {
		getController().setGameState(newGameState);
	}

	/**
	 * Displays an Alert to the user containing the given message.
	 *
	 * @param s String alert message
	 */
	public void sendAlert(String s) {
		Alert myAlert;
		myAlert = new Alert(AlertType.INFORMATION);
		myAlert.setTitle("Information Dialog");
		myAlert.setHeaderText(null);
		myAlert.setContentText(s);
		myAlert.showAndWait();
	}

	private void placePanes() {
		initPanes();
		SplitPane innerSplitPane = new SplitPane(worldView.getObject(), new VBox(new MinimapPane(worldView.getGridPane(), getController()).getObject(), tempPane.getObject()));
		innerSplitPane.setDividerPositions(1);
		innerSplitPane.setOrientation(Orientation.HORIZONTAL);
		outerSplitPane = new SplitPane(menuBar.getObject(), innerSplitPane, detailPane.getObject());
		outerSplitPane.setDividerPositions(0, .8);
		outerSplitPane.setOrientation(Orientation.VERTICAL);
		SplitPane.setResizableWithParent(menuBar.getObject(), false);		//In case user is on Windows and MenuBar is in the View
	}

	/**
	 * Initializes all panes in the GUI and makes View a listener to all
	 * necessary panes.
	 */
	private void initPanes() {
		menuBar = new VoogaMenuBar(this, getController(), editable);
		menuBar.getStyleSheet().addListener((observable, oldValue, newValue) -> {
			getObject().getStylesheets().clear();
			getObject().getStylesheets().add(newValue);
		});
		worldView = WorldViewFactory.newWorldView(getController());
		detailPane = DetailPaneFactory.newDetailPane();
		tempPane = TemplatePaneFactory.newTemplatePane(getController(),
				new MinimapPane(worldView.getGridPane(), getController()));
		gameObserver = GameObserverFactory.newGameObserver(getController(), worldView, detailPane, tempPane);
		detailPane.addDetailPaneObserver(gameObserver);
		tempPane.addTemplatePaneObserver(gameObserver);
		worldView.addWorldViewObserver(gameObserver);
		worldView.addGridViewObserver(gameObserver);
		worldView.addCellViewObserver(gameObserver);
		worldView.addUnitViewObserver(gameObserver);
		// rulesPane = new RulesPane(); //TODO For when rules pane is created
	}

	/**
	 * Performs all necessary actions to convert the View into development mode.
	 * If the View is already in development mode, then nothing visually
	 * changes.
	 */
	private void enterAuthorMode() {
		// addSidePanes();
	}

	/**
	 * Performs all necessary actions to convert the View into play mode. If the
	 * View is already in play mode, then nothing visually changes.
	 */
	private void enterPlayMode() {

		// removeSidePanes();
	}

	// /**
	// * Adds the ToolsPane and TemplatePane to the sides of the View's GUI.
	// */
	// private void addSidePanes() {
	// innerSplitPane.getItems().add(tempPane.getObject());
	// }
	//
	// /**
	// * Removes the ToolsPane and TemplatePane from the sides of the View's
	// GUI.
	// */
	// private void removeSidePanes() {
	// innerSplitPane.getItems().remove(tempPane.getObject());
	// }

	public static Image getImg(String imgPath) {
		if (!IMAGE_CACHE.containsKey(imgPath)) {
			try {
				IMAGE_CACHE.put(imgPath, new Image(imgPath));
			} catch (Exception e) {
				System.out.println("Error opening image: " + imgPath);
				IMAGE_CACHE.put(imgPath, IMAGE_CACHE.get(""));
			}
		}
		return IMAGE_CACHE.get(imgPath);
	}

	@Override
	public void update() {
		super.update();
		this.setEditable(getController().getAuthoringGameState().isAuthoringMode());
	}
}