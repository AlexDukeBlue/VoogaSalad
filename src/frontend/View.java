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
 * @author Stone Mathers
 * Created 4/3/2017
 */
package frontend;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import backend.player.Team;
import backend.util.GameplayState;
import controller.Controller;
import frontend.factory.abilitypane.AbilityPane;
import frontend.factory.conditionspane.ConditionsPaneFactory;
import frontend.factory.detailpane.DetailPaneFactory;
import frontend.factory.templatepane.TemplatePaneFactory;
import frontend.factory.worldview.MinimapPane;
import frontend.factory.worldview.WorldViewFactory;
import frontend.interfaces.conditionspane.ConditionsPaneExternal;
import frontend.interfaces.detailpane.DetailPaneExternal;
import frontend.interfaces.templatepane.TemplatePaneExternal;
import frontend.interfaces.worldview.WorldViewExternal;
import frontend.menubar.VoogaMenuBar;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import polyglot.PolyglotException;

public class View extends ClickableUIComponent<Region> {
	private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();
	private static final int CONDITIONS_PANE_POS = 0;
	private Stage myStage;

	static {
		IMAGE_CACHE.put("", new Image("resources/images/transparent.png"));
	}

	private SplitPane outerSplitPane;
	private SplitPane innerSplitPane;
	private SplitPane bottomPane;
	private SplitPane worldAndDetailPane;
	private Button endTurnButton;
	private VoogaMenuBar menuBar;
	private WorldViewExternal worldView;
	private DetailPaneExternal detailPane;
	private AbilityPane abilityPane;
	private TemplatePaneExternal tempPane;
	private ConditionsPaneExternal conditionsPane;
	private VBox rightPane;

	public View(Controller controller) {
		this(controller, new Stage());
	}

	public View(Controller controller, Stage stage) {
		super(controller, null);
		myStage = stage;
		placePanes();
		getStyleSheet().setValue(getPossibleStyleSheetNamesAndFileNames().get("DefaultTheme"));
	}

	private void setViewEditable(boolean editable) {
		if (editable) {
			enterAuthorMode();
		} else if (!editable) {
			enterPlayMode();
		}
	}

	public void toggleConditionsPane() {
		if (!innerSplitPane.getItems().remove(conditionsPane.getNode())) {
			innerSplitPane.getItems().add(CONDITIONS_PANE_POS, conditionsPane.getNode());
		}
	}

	public void toggleTemplatePane() {
		if (!innerSplitPane.getItems().remove(rightPane)) {
			innerSplitPane.getItems().add(rightPane);
		}
	}

	public void toggleDetailsPane() {
		if (!worldAndDetailPane.getItems().remove(bottomPane)) {
			worldAndDetailPane.getItems().add(bottomPane);
		}
	}

	public void toggleStatsPane() {
		// TODO
	}

	@Override
	public Region getNode() {
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
	 * @param newGameState
	 *            GameplayState that the View will now access its data from
	 */
	public void setGameState(GameplayState newGameState) {
		getController().setGameState(newGameState);
	}

	private void placePanes() {
		initPanes();
		endTurnButton = new Button(getPolyglot().get("EndTurn").getValueSafe());
		endTurnButton.setOnMouseClicked(e -> {
			getClickHandler().cancel();
			getController().endTurn();
		});
		endTurnButton.setMinWidth(70);
		endTurnButton.setPadding(new Insets(5, 2, 5, 2));
		endTurnButton.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		ImageView cancelImg = new ImageView(View.getImg(getResourceBundle().getString("cancelImgPath")));
		cancelImg.setPreserveRatio(true);
		cancelImg.setSmooth(true);
		cancelImg.setFitWidth(50);
		Button cancelButton = new Button("", cancelImg);
		cancelButton.setCancelButton(true);
		cancelButton.setPadding(Insets.EMPTY);
		cancelButton.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		cancelButton.setOnMouseClicked(event -> {
			getClickHandler().showDetail(null);
			getClickHandler().cancel();
		});
		VBox box = new VBox(endTurnButton, cancelButton);
		box.setPadding(Insets.EMPTY);
		box.setSpacing(0);
		box.setFillWidth(true);
		VBox.setVgrow(endTurnButton, Priority.ALWAYS);
		VBox.setVgrow(cancelButton, Priority.ALWAYS);
		bottomPane = new SplitPane(detailPane.getNode(), abilityPane.getNode(), box);
		bottomPane.setOrientation(Orientation.HORIZONTAL);
		box.prefHeightProperty().bind(bottomPane.heightProperty());
		box.prefWidthProperty().bind(box.widthProperty());
		worldAndDetailPane = new SplitPane(worldView.getNode(), bottomPane);
		worldAndDetailPane.setOrientation(Orientation.VERTICAL);
		innerSplitPane = new SplitPane(conditionsPane.getNode(), worldAndDetailPane, rightPane);
		innerSplitPane.setOrientation(Orientation.HORIZONTAL);
		outerSplitPane = new SplitPane(menuBar.getNode(), innerSplitPane);
		outerSplitPane.setOrientation(Orientation.VERTICAL);
		outerSplitPane.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ESCAPE)) {
				getClickHandler().showDetail(null);
				getClickHandler().cancel();
			}
		});
		setDividerPositions();
		SplitPane.setResizableWithParent(menuBar.getNode(), false);

	}

	private void setDividerPositions() {
		bottomPane.setDividerPositions(getDoubleFromResourceBundle("BottomPaneDividerPosition1"),
				getDoubleFromResourceBundle("BottomPaneDividerPosition2"));
		worldAndDetailPane.setDividerPositions(getDoubleFromResourceBundle("WorldAndDetailPaneDividerPosition1"));
		innerSplitPane.setDividerPositions(getDoubleFromResourceBundle("innerSplitPaneDividerPosition1"),
				getDoubleFromResourceBundle("innerSplitPaneDividerPosition2"));
		outerSplitPane.setDividerPositions(getDoubleFromResourceBundle("outerSplitPaneDividerPosition1"));
	}

	private double getDoubleFromResourceBundle(String key) {
		return Double.parseDouble(getResourceBundle().getString(key));
	}

	/**
	 * Initializes all panes in the GUI and makes View a listener to all
	 * necessary panes.
	 */
	private void initPanes() {
		menuBar = new VoogaMenuBar(this, getController(), getController().isAuthoringMode());
		menuBar.getStyleSheet().addListener((observable, oldValue, newValue) -> {
			getNode().getStylesheets().clear();
			getNode().getStylesheets().add(newValue);
		});
		worldView = WorldViewFactory.newWorldView(getController(), getClickHandler());
		detailPane = DetailPaneFactory.newDetailPane(getController(), getClickHandler());
		abilityPane = new AbilityPane(getController(), getClickHandler());
		setClickHandler(
				new ClickHandler(detailPane, abilityPane, worldView.getGridView(), ClickHandler.Mode.AUTHORING));
		tempPane = TemplatePaneFactory.newTemplatePane(getController(), getClickHandler());
		rightPane = new VBox(new MinimapPane(worldView.getGridView().getNode(), getController()).getNode(),
				tempPane.getNode());
		conditionsPane = ConditionsPaneFactory.newConditionsPane(getController(), getClickHandler());
		menuBar.getPolyglot().setOnLanguageChange(event -> {
			try {
				worldView.getPolyglot().setLanguage(menuBar.getPolyglot().getLanguage());
				detailPane.getPolyglot().setLanguage(menuBar.getPolyglot().getLanguage());
				abilityPane.getPolyglot().setLanguage(menuBar.getPolyglot().getLanguage());
				tempPane.getPolyglot().setLanguage(menuBar.getPolyglot().getLanguage());
				conditionsPane.getPolyglot().setLanguage(menuBar.getPolyglot().getLanguage());
			} catch (PolyglotException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(getPolyglot().get("LanguageError").getValue());
				alert.show();
			}
		});
	}

	/**
	 * Performs all necessary actions to convert the View into authoring mode.
	 * If the View is already in authoring mode, then nothing visually changes.
	 */
	private void enterAuthorMode() {
		addSidePanes();
		getClickHandler().setMode(ClickHandler.Mode.AUTHORING);
		getClickHandler().cancel();
		detailPane.setAuthorMode();
		setDividerPositions();
	}

	private void addSidePanes() {
		if (!innerSplitPane.getItems().contains(conditionsPane.getNode())) {
			innerSplitPane.getItems().add(CONDITIONS_PANE_POS, conditionsPane.getNode());
		}
		if (!innerSplitPane.getItems().contains(rightPane)) {
			innerSplitPane.getItems().add(rightPane);
		}
	}

	/**
	 * Performs all necessary actions to convert the View into play mode. If the
	 * View is already in play mode, then nothing visually changes.
	 */
	private void enterPlayMode() {
		// TODO Don't remove the minimap!
		removeSidePanes();
		getClickHandler().cancel();
		getClickHandler().setMode(ClickHandler.Mode.GAMEPLAY);
		detailPane.setPlayMode();
		setDividerPositions();
	}

	private void removeSidePanes() {
		innerSplitPane.getItems().remove(conditionsPane.getNode());
		innerSplitPane.getItems().remove(rightPane);
	}

	public void joinTeam() {
		ChoiceDialog<Team> teams = new ChoiceDialog<>(getController().getMyPlayer().getTeam().orElse(null),
				getController().getReadOnlyGameState().getTeams());
		teams.headerTextProperty().bind(getPolyglot().get("JoinTeamMessage"));
		teams.titleProperty().bind(getPolyglot().get("JoinTeamTitle"));
		Optional<Team> chosenTeam = teams.showAndWait();
		try{
			getController().joinTeam(chosenTeam.get().getName());
		} catch (Exception e) {
			if(!getController().getMyPlayer().getTeam().isPresent() && !getController().isAuthoringMode()){
				Alert alert = new Alert(AlertType.ERROR);
				alert.titleProperty().bind(getPolyglot().get("NoTeamSelectedTitle"));
				alert.headerTextProperty().bind(getPolyglot().get("NoTeamSelectedHeader"));
				alert.showAndWait();
				getController().enterAuthoringMode();
			} else {
				//Do nothing
			}
		}
	}

	@Override
	public void setClickHandler(ClickHandler clickHandler) {
		super.setClickHandler(clickHandler);
		abilityPane.setClickHandler(clickHandler);
		worldView.setClickHandler(clickHandler);
		detailPane.setClickHandler(clickHandler);
	}

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
		this.setViewEditable(getController().isAuthoringMode());
		endTurnButton.setDisable(!getController().isMyPlayerTurn() || getController().isAuthoringMode());
		if (!getController().getMyPlayer().getTeam().isPresent() && !getController().isAuthoringMode()) {
			joinTeam();
		}
	}
}