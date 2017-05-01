package frontend;

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
import frontend.startup.StartupScreen;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.polyglot.PolyglotException;
import java.util.Objects;
import java.util.Optional;

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
public class View extends ClickableUIComponent<Region> {
	private static final int CONDITIONS_PANE_POS = 0;
	private final Stage myStage;

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
	private ChoiceDialog<Team> teams;
	private MinimapPane miniMap;

	/**
	 * Constructs a View using the passed Controller and a new Stage.
	 * 
	 * @param controller
	 *            Controller that is used to communicate with the Model.
	 */
	public View(Controller controller) {
		this(controller, new Stage());
	}

	/**
	 * Constructs a View using the passed parameters.
	 * 
	 * @param controller
	 *            Controller that is used to communicate with the Model.
	 * @param stage
	 *            Stage on which the View is displayed.
	 */
	public View(Controller controller, Stage stage) {
		super(controller, null);
		myStage = stage;
		placePanes();
		menuBar.getStyleSheet().setValue(getPossibleStyleSheetNamesAndFileNames().get("DarkTheme"));
//		menuBar.getStyleSheet().setValue(getPossibleStyleSheetNamesAndFileNames().get("DefaultTheme"));
	}

	/**
	 * Toggles whether or not the ConditionsPane is visible.
	 */
	public void toggleConditionsPane() {
		if (!innerSplitPane.getItems().remove(conditionsPane.getNode())) {
			innerSplitPane.getItems().add(CONDITIONS_PANE_POS, conditionsPane.getNode());
		}
	}

	/**
	 * Toggles whether or not the TemplatePane is visible.
	 */
	public void toggleTemplatePane() {
		if (!innerSplitPane.getItems().remove(rightPane)) {
			innerSplitPane.getItems().add(rightPane);
		}
	}

	/**
	 * Toggles whether or not the DetailsPane is visible.
	 */
	public void toggleDetailsPane() {
		if (!worldAndDetailPane.getItems().remove(bottomPane)) {
			worldAndDetailPane.getItems().add(bottomPane);
		}
	}

	/**
	 * Toggles whether or not the StatsPane is visible.
	 */
	public void toggleStatsPane() {
		// TODO
	}

	@Override
	public Region getNode() {
		return outerSplitPane;
	}

	/**
	 * Return the Stage on which the View is being displayed.
	 * 
	 * @return Stage
	 */
	public Stage getStage() {
		return myStage;
	}

	/**
	 * Sets the GameState from which the View accesses its data.
	 *
	 * @param newGameState
	 *            GameplayState from which the View will now access its data.
	 */
	public void setGameState(GameplayState newGameState) {
		getController().setGameState(newGameState);
	}

	public void joinTeam() {
		teams = new ChoiceDialog<>(getController().getMyPlayer().getTeam().orElse(null),
				getController().getReadOnlyGameState().getTeams());
		teams.headerTextProperty().bind(getPolyglot().get("JoinTeamMessage"));
		teams.titleProperty().bind(getPolyglot().get("JoinTeamTitle"));
		Optional<Team> chosenTeam = teams.showAndWait();
		try {
			getController().joinTeam(chosenTeam.get().getName());
		} catch (Exception e) {
			if (!getController().getMyPlayer().getTeam().isPresent() && !getController().isAuthoringMode()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.titleProperty().bind(getPolyglot().get("NoTeamSelectedTitle"));
				alert.headerTextProperty().bind(getPolyglot().get("NoTeamSelectedHeader"));
				alert.showAndWait();
				getController().enterAuthoringMode();
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

	@Override
	public void update() {
		this.setViewEditable(getController().isAuthoringMode());
		endTurnButton.setDisable(!getController().isMyTeam() || getController().isAuthoringMode());
		if (!getController().getMyPlayer().getTeam().isPresent() && !getController().isAuthoringMode()
				&& (Objects.isNull(teams) || !teams.isShowing())) {
			joinTeam();
		}
		if((!getController().isAuthoringMode()) && Objects.nonNull(getController().getActiveTeam())){
			checkForCondition();
		}
	}

	private void setViewEditable(boolean editable) {
		if (editable) {
			enterAuthorMode();
		} else {
			enterPlayMode();
		}
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
		ImageView cancelImg = new ImageView(getImg(getResourceBundle().getString("cancelImgPath")));
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
		setMaxWidthsAndHeights();
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

	private void setMaxWidthsAndHeights() {
		rightPane.maxWidthProperty()
		.bind(outerSplitPane.widthProperty().multiply(getDoubleFromResourceBundle("RightPaneWidthMultiplier")));
		conditionsPane.getNode().maxWidthProperty().bind(
				outerSplitPane.widthProperty().multiply(getDoubleFromResourceBundle("ConditionsPaneWidthMultiplier")));
		bottomPane.maxHeightProperty().bind(
				outerSplitPane.heightProperty().multiply(getDoubleFromResourceBundle("BottomPaneHeightMultiplier")));
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
		worldView = WorldViewFactory.newWorldView(getController(), getClickHandler());
		detailPane = DetailPaneFactory.newDetailPane(getController(), getClickHandler());
		abilityPane = new AbilityPane(getController(), getClickHandler());
		setClickHandler(
				new ClickHandler(detailPane, abilityPane, worldView.getGridView(), ClickHandler.Mode.AUTHORING));
		tempPane = TemplatePaneFactory.newTemplatePane(getController(), getClickHandler());
		miniMap = new MinimapPane(worldView.getGridView().getNode(), getController());
		tempPane.getNode().prefWidthProperty().bind(miniMap.getNode().widthProperty());
		rightPane = new VBox(miniMap.getNode(), tempPane.getNode());
		conditionsPane = ConditionsPaneFactory.newConditionsPane(getController(), getClickHandler());
		getStyleSheet().bind(menuBar.getStyleSheet());
		worldView.getStyleSheet().bind(getStyleSheet());
		detailPane.getStyleSheet().bind(getStyleSheet());
		abilityPane.getStyleSheet().bind(getStyleSheet());
		tempPane.getStyleSheet().bind(getStyleSheet());
		conditionsPane.getStyleSheet().bind(getStyleSheet());
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
		detailPane.setAuthorMode();
		setDividerPositions();
	}

	private void addSidePanes() {
		if (!innerSplitPane.getItems().contains(conditionsPane.getNode())) {
			innerSplitPane.getItems().add(CONDITIONS_PANE_POS, conditionsPane.getNode());
		}
		if (!rightPane.getChildren().contains(tempPane.getNode())) {
			rightPane.getChildren().add(tempPane.getNode());
		}
	}

	/**
	 * Performs all necessary actions to convert the View into play mode. If the
	 * View is already in play mode, then nothing visually changes.
	 */
	private void enterPlayMode() {
		removeSidePanes();
		getClickHandler().setMode(ClickHandler.Mode.GAMEPLAY);
		detailPane.setPlayMode();
		setDividerPositions();
	}

	private void removeSidePanes() {
		innerSplitPane.getItems().remove(conditionsPane.getNode());
		rightPane.getChildren().remove(tempPane.getNode());
	}

	private void checkForCondition() {
		if (getController().activeTeamWon()) {
			displayEndPopup(getPolyglot().get("WinMessage"));
		} else if (getController().activeTeamLost()) {
			displayEndPopup(getPolyglot().get("LoseMessage"));
		} else if (getController().activeTeamTied()) {
			displayEndPopup(getPolyglot().get("TieMessage"));
		}
	}

	private void displayEndPopup(StringBinding message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.headerTextProperty().bind(message);
		alert.contentTextProperty().bind(getPolyglot().get("EndMessage"));
		alert.showAndWait();
		myStage.setScene(new Scene(new StartupScreen(myStage).getNode()));
	}
	
}