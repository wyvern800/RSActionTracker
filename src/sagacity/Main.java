package sagacity;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;
import objects.*;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import utils.*;
import javafx.scene.image.Image;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Software used to log the actions from the player (this is not a keylogger)
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 04/12/2020 - 14:14
 * @project RSActionsLogging
 */
public class Main extends Application implements NativeKeyListener, Constants, MasksConstants {
    /**
     * The title
     */
    private static final String TITLE = "RSActionLogger - by wyvern800"; //Do not touch

    public static Desktop desktop = Desktop.getDesktop();

    /**
     * The combat style you'l be using on the abilities
     * ActionStyle.MELEE For melee | ActionStyle.RANGED for ranged | ActionStyle.MAGIC for magic
     */
    private static ActionStyle actionStyle;

    // The actionStatus
    private static ActionStatus actionStatus = ActionStatus.IDLE;

    // The configTable
    private static TableView<Action> setupTableView;
    private static TableView<ActionTier> actionTierTableView;
    private static TableView<ActionStyle> actionStyleTableView;

    /**
     * Cached actions
     */
    public static List<Action> cachedActions;
    public static List<ActionTier> cachedActionTiers;
    public static List<ActionStyle> cachedActionStyles;

    /**
     * The personal pressed actions (that is used to store session
     */
    public static List<Action> actions;

    /**
     * {@code True} if in keys should be logged, {@code False} if not
     */
    //private static boolean isCombatMode = false;
    private static boolean showActionName = true;
    private static boolean showPatreonMsg = true;
    private static boolean showAbilityBorder = true;
    //private static boolean setKeyBindMode = false;

    /**
     * Variables
     */
    private static Stage mainStage;
    private static int actionsDone;
    public static LastKeyPressed lastKeyPressed;
    private static SavedData savedData;

    /**
     * The grid pane
     */
    public static GridPane mainGridPane;

    /**
     * Max actions we're monitoring
     */
    private static final int MAX_SIZE = 10; //Do not touch

    /**
     * Gets the saved data
     * @return The savedData
     */
    public static SavedData getSavedData() {
        return savedData;
    }

    /**
     * Sets the current actionStatus
     * @param actionStatus the actionStatus
     */
    public static void setActionStatus(ActionStatus actionStatus) {
        Main.actionStatus = actionStatus;
    }

    /**
     * Gets the current actionStatus
     * @return the actionStatus
     */
    public static ActionStatus getActionStatus() {
        return actionStatus;
    }

    /**
     * The constructor
     */
    public Main() {
        // Initializations
        cachedActions = new ArrayList<>();
        cachedActionTiers = new ArrayList<>();
        cachedActionStyles = new ArrayList<>();
        actions = new ArrayList<>();

        loadDatabase();

        cachedActionTiers = getSavedData().getCachedActionTiers();
        cachedActionStyles = getSavedData().getCachedActionStyles();
        cachedActions = getSavedData().getCachedActions();
        actionStyle = getSavedData().getActionStyle();
        showActionName = getSavedData().isShowActionName();
        showAbilityBorder = getSavedData().isShowAbilityBorder();

        // Adds the jNativeHook listener
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        // Logging
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        GlobalScreen.addNativeKeyListener(this);
    }

    /**
     * Add an action to the screen
     * @param action The action we're adding
     * @param nativeKeyEvent The nativeKeyEvent
     */
    private static void addActionToScreen(Action action, NativeKeyEvent nativeKeyEvent) {
        // Handles the keys pressing
        actions.add(action);

        if (actions.size() > MAX_SIZE) {
            actions.remove(0); // Removes the first element if list is full
            mainGridPane.getChildren().remove(0);
            ObservableList<Node> copy = mainGridPane.getChildren();
            copy = new SortedList<>(copy);
            mainGridPane.getChildren().clear();
            mainGridPane.getChildren().addAll(copy);
        }

        actionsDone++;

        FileWritter.write(TOTAl_ACTIONS[0], TOTAl_ACTIONS[1], new String[]{Integer.toString(actionsDone)});
        lastKeyPressed = new LastKeyPressed(LocalTime.now(), nativeKeyEvent.getKeyCode());
        updateTitle(mainStage);

        // updates the screen
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) == null)
                continue;

            Action actionToBeAdded = actions.get(i);

            // Creating the action instance
            //Action newAction = new Action(/*configTable,*/ filteredAction.getActionName(), filteredAction.getPressedKey(), filteredAction.isCtrlPressed(), filteredAction.isShiftPressed(), filteredAction.isAltPressed(), filteredAction.getActionTier(), filteredAction.getIconPath(), filteredAction.getActionStyle());

            // Sets the actionImage to the action
            actionToBeAdded.setActionImage(new ImageView(new Image(new File(actionToBeAdded.getIconPath()).toURI().toString())));

            // Sets the image size to fit the screen
            actionToBeAdded.getActionImage().setFitWidth(104);
            actionToBeAdded.getActionImage().setFitHeight(104);

            // Creating the content boxes
            HBox actionBox = new HBox(1);
            Group actionGroup = new Group();

            // Sets the backgrounds of the actionBoxes to black
            actionGroup.setStyle("-fx-background-color: #000000;");
            actionBox.setStyle("-fx-background-color: #000000;");

            // Creating the actionName label
            Label actionName = new Label(actionToBeAdded.getActionName());
            actionName.setStyle("-fx-text-fill: #fff; -fx-font-size: 15px; -fx-effect: dropshadow( one-pass-box , black , 10 , 0.0 , 2 , 0 )");
            actionName.setAlignment(Pos.CENTER);
            actionName.setPrefSize(100, 100);
            actionBox.setSpacing(2); // Space between the squares
            actionBox.setPrefSize(60, 60);


            actionBox.setStyle("-fx-border-style: solid solid solid solid;\n" +
                        "-fx-border-width: 2;\n" +
                        "-fx-border-color: " + (showAbilityBorder ? actionToBeAdded.getActionTier().getAbilityBorder() : "black"));


            actionToBeAdded.getActionImage().setFitWidth(106);
            actionToBeAdded.getActionImage().setFitHeight(106);
            actionToBeAdded.getActionImage().setY(-3);
            actionToBeAdded.getActionImage().setX(-1);
            actionGroup.getChildren().add(actionToBeAdded.getActionImage());
            actionGroup.getChildren().add(actionName);

            actionBox.getChildren().add(actionGroup);

            actionName.setVisible(showActionName);

            // Add the actionBox to the gridPane
            mainGridPane.add(actionBox, i, 0);
            System.out.println("actions on screen: "+mainGridPane.getChildren().size());
        }
    }

    private static Menu stopResume = new Menu("Stop");
    private static final String[][] stopResumeLabel = {{"Start Logging", "play.png"}, {"Stop Logging", "stop.png"}, {"Resume Logging", "play.png"}};

    /**
     * Starts the program
     *
     * @param mainStage The main stage
     */
    @Override
    public void start(Stage mainStage) {
        setupTableView = new TableView<>();
        actionTierTableView = new TableView<>();
        actionStyleTableView = new TableView<>();
        actionTierTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        actionStyleTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // create the menus
        final Menu menu = new Menu("Options", getMenuIcon("list.png"));
        final Menu configurations = new Menu("Setup", getMenuIcon("setup.png"));
        final Menu links = new Menu("Links", getMenuIcon("help.png"));
        final MenuItem help = new MenuItem("Help", getMenuIcon("config.png"));
        final MenuItem about = new MenuItem("About", getMenuIcon("config.png"));
        final MenuItem discord = new MenuItem("Discord", getMenuIcon("config.png"));
        addMenuItemAction(discord, ()-> sendOpenURL("https://discord.gg/yaUHKTWJSJ"));
        final MenuItem report = new MenuItem("Bugs Report", getMenuIcon("bug.png"));
        addMenuItemAction(report, ()-> sendOpenURL("https://github.com/wyvern800/RSActionLogger/issues"));
        final MenuItem purchase = new MenuItem("Purchase a License", getMenuIcon("config.png"));
        purchase.setDisable(true);
        final SeparatorMenuItem sep2 = new SeparatorMenuItem();
        links.getItems().addAll(help, discord, about, report, sep2, purchase);
        stopResume = new Menu(stopResumeLabel[0][0], getMenuIcon(stopResumeLabel[0][1]));
        addMenuItemAction(help, ()-> sendOpenURL("https://github.com/wyvern800/RSActionLogger/blob/master/README.md"));
        addMenuItemAction(about, ()-> {
            System.out.println("about");
            sendPatreonPopup(true);
        });
        addMenuItemAction(purchase, ()-> System.out.println("purchase"));
        // Combat menu
        //final MenuItem toggleCombatMode = new MenuItem("Toggle Idle mode", getMenuIcon("config.png"));
        //final MenuItem toggleAbilityName = new MenuItem("Toggle display ability name", getMenuIcon("config.png"));
        final MenuItem settings = new MenuItem("Settings", getMenuIcon("config.png"));
        // add menu items to menu
        menu.getItems().addAll(settings/*toggleCombatMode, toggleAbilityName*/);

        addMenuItemAction(settings, Main::openSettingsScreen);

        /*addMenuItemAction(toggleCombatMode, Main::toggleIdleMode);

        addMenuItemAction(toggleAbilityName, () -> {
            showActionName = !showActionName;
            System.out.println("Ability name is now " + (showActionName ? "enabled" : "disabled"));
        });*/

        addMenuAction(stopResume, Main::toggleIdleMode);

        // Configurations menu
        final MenuItem configureAbilities = new MenuItem("Actions", getMenuIcon("click.png"));
        final MenuItem actionStyle = new MenuItem("ActionStyles", getMenuIcon("config.png"));
        final MenuItem actionTier = new MenuItem("ActionTiers", getMenuIcon("config.png"));
        actionStyle.setDisable(true);
        actionTier.setDisable(true);
        addMenuItemAction(configureAbilities, ()-> {
            //toggleIdleMode();
            stopLoggingMode();
            openSetupScreen();
        });
        addMenuItemAction(actionStyle, Main::openActionStyleScreen);
        addMenuItemAction(actionTier, Main::openActionTierScreen);
        final SeparatorMenuItem sep5 = new SeparatorMenuItem();
        configurations.getItems().addAll(configureAbilities, sep5, actionTier, actionStyle);

        // add menu to menu bar
        // Left bar
        MenuBar leftBar = new MenuBar();
        leftBar.getMenus().addAll(menu, configurations, links);
        // Right bar
        MenuBar rightBar = new MenuBar();
        rightBar.getMenus().addAll(stopResume);

        Region spacer = new Region();
        spacer.getStyleClass().add("menu-bar");
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        // Add the menuBars to screen
        HBox menuBars = new HBox(leftBar, spacer, rightBar);

        // create a VBox
        VBox verticalBox = new VBox(menuBars);
        // create a scene
        Scene mainScene = new Scene(verticalBox, 1100, 135);
        mainScene.getStylesheets().add("utils/dark-theme.css");
        // set a background color to the vertical box
        verticalBox.setStyle("-fx-background-color: #ff14f7;");
        // create a grid pane
        mainGridPane = new GridPane();
        // add the grid pane to the vertical box
        verticalBox.getChildren().add(mainGridPane);

        mainStage.setTitle(TITLE);
        mainStage.setScene(mainScene);
        mainStage.setAlwaysOnTop(true);
        mainStage.setResizable(false);
        mainStage.centerOnScreen();
        mainStage.sizeToScene();
        mainStage.show();

        // Center the window to screen
        centerScreen(mainStage);
        addStageIcon(mainStage);

        Main.mainStage = mainStage;

        sendPatreonPopup(false);

        stopResume.setMnemonicParsing(true);
        mainStage.getScene().getAccelerators().put(
                new KeyCodeCombination(KeyCode.F12),
                () -> {
                    stopResume.fire();
                    toggleIdleMode();
                }
        );

        addCloseEventHandler(mainStage, true);
    }

    /**
     * Sends a opening URL packet
     * @param URL The url to be opened
     */
    private static void sendOpenURL(String URL) {
        try {
            Desktop.getDesktop().browse(new URL(URL).toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an icon to the stage
     */
    private static void addStageIcon(Stage stage) {
        Image anotherIcon = new Image(ICONS_PATH + "/" + "log.png");
        stage.getIcons().add(anotherIcon);
    }

    /**
     * Centers the window
     *
     * @param stage The stage
     */
    private static void centerScreen(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    /**
     * Sends a popup to tell people to support
     */
    private static void sendPatreonPopup(boolean byMenu) {
        if (!showPatreonMsg && !byMenu)
            return;
        showLinkToPatreonDialog("RSActionLogger app. created by Matheus G. Ferreira",
                "I hope you enjoy using my lovely app!",
                "Have fun using my software, if you like, please consider becoming my patron on Patreon ♥ \n \n I'd love drinking a coffee with your donations! (Take in consideration " +
                        "that helping isn't necessary but appreciated, so feel free to do whatever you prefer to!\n \n- Also if you want to support me directly, this is my PayPal email: wyvern800@hotmail.com,\n" +
                        "- I'd also accept RSGP, so if you want, my discord tag is 'ferreirA#1058', thank you in advance! \n \n Git gud with ur dpsing u nooby scaper =]",
                mainStage,
                ()-> {
                    sendOpenURL("https://www.patreon.com/join/wyvern800/checkout?ru=undefined");
                });
    }

    /**
     * Constructs the setup stage tableView
     * @param ownerStage Who called this tableView
     */
    private static void constructSetupTableView(Stage ownerStage) {
        ObservableList<Action> observableListData = FXCollections.observableArrayList(getSavedData().getCachedActions());

        cachedActions.forEach(a-> {
            a.setActionImage(new ImageView(new Image(new File(a.getIconPath()).toURI().toString())));
            //refreshTable();
        });

        setupTableView.setEditable(true);
        setupTableView.setFocusTraversable(false);
        setupTableView.getSelectionModel().selectedItemProperty().addListener((obs) -> setupTableView.requestFocus());

        // Add the actionName
        TableColumn actionName = addTableColumn(new TableColumn<Action, String>("ActionName"), null, new PropertyValueFactory<Action, String>("actionName"), true);
        actionName.setCellFactory(
                TextFieldTableCell.forTableColumn());
        actionName.setStyle("-fx-align: center");
        actionName.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, String>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setActionName(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setActionName(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );

        // Add the keyCode
        TableColumn keyCode = addTableColumn(new TableColumn<Action, String>("Key"), null, new PropertyValueFactory<Action, String>("pressedKeyName"), false);
        /*keyCode.setCellFactory(
                TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        keyCode.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, Integer>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setPressedKey(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setPressedKey(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );*/

        // Add the ctrlMask
       /*ctrlMask.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
        ctrlMask.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, Boolean>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setCtrlPressed(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setCtrlPressed(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );*/
        TableColumn ctrlMask = addTableColumn(new TableColumn<Action, Boolean>("CTRL?"), null, new PropertyValueFactory<Action, Boolean>("ctrlBooleanProperty"), true);
        ctrlMask.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            //System.out.println(observableListData.get(param).getActionName()+" CTRL checkbox changed value to "+observableListData.get(param).isCtrlCheckboxChecked());

            observableListData.get(param).setCtrlBooleanProperty(observableListData.get(param).isCtrlCheckboxChecked());


            getSavedData().saveData();
            return observableListData.get(param).ctrlBooleanPropertyProperty();
        }));


        // Add the shiftMask
        /*TableColumn shiftMask = addTableColumn(new TableColumn<Action, Boolean>("Shift?"), null, new PropertyValueFactory<Action, Boolean>("shiftPressed"), true);
        shiftMask.setCellFactory(
                TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
        shiftMask.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Action, Boolean>>) t -> {

                    if (!t.getNewValue() && t.getNewValue()) {
                        System.out.println("error");
                        showErrorDialog("Invalid entry, this must be whether 'false' or 'true'", ownerStage, ()-> {
                            setupTableView.getFocusModel().focus(new TablePosition<Action, Boolean>(setupTableView, setupTableView.getSelectionModel().getFocusedIndex(),null));
                        });
                        return;
                    }
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setShiftPressed(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setShiftPressed(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );*/
        TableColumn shiftMask = addTableColumn(new TableColumn<Action, Boolean>("Shift?"), null, new PropertyValueFactory<Action, Boolean>("shiftBooleanProperty"), true);
        shiftMask.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            //System.out.println(observableListData.get(param).getActionName()+" SHIFT checkbox changed value to "+observableListData.get(param).isShiftCheckboxChecked());

            observableListData.get(param).setShiftBooleanProperty(observableListData.get(param).isShiftCheckboxChecked());

            getSavedData().saveData();
            return observableListData.get(param).shiftBooleanPropertyProperty();
        }));
        // Add the altMask
           /*TableColumn altMask = addTableColumn(new TableColumn<Action, Boolean>("Alt?"), null, new PropertyValueFactory<Action, Boolean>("altPressed"), true);
        altMask.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
        altMask.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, Boolean>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setAltPressed(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setAltPressed(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );*/
        TableColumn altMask = addTableColumn(new TableColumn<Action, Boolean>("Alt?"), null, new PropertyValueFactory<Action, Boolean>("altBooleanProperty"), true);
        altMask.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            //System.out.println(observableListData.get(param).getActionName()+" ALT checkbox changed value to "+observableListData.get(param).isAltCheckboxChecked());

            observableListData.get(param).setAltBooleanProperty(observableListData.get(param).isAltCheckboxChecked());

            getSavedData().saveData();
            return observableListData.get(param).altBooleanPropertyProperty();
        }));

        // Add the actionTier
        TableColumn actionTier = addTableColumn(new TableColumn<Action, ActionTier>("ActionTier"), null, new PropertyValueFactory<Action, ActionTier>("actionTier"), true);
        actionTier.setCellFactory(ComboBoxTableCell.forTableColumn(ActionTier.values()));
        /*actionTier.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter() {
            @Override
            public String toString(Object interpolator) {
                String str = interpolator.toString();
                return str.substring(str.indexOf(".") + 1);
            }

            @Override
            public ActionTier fromString(String string) {
                return ActionTier.getWrappedActionTier(string);
            }
        }));*/
        actionTier.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, ActionTier>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setActionTier(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setActionTier(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );
        TableColumn actionImage = addTableColumn(new TableColumn<Action, ImageView>("Icon"), null, new PropertyValueFactory<Action, ImageView>("actionImage"), false);
        /*actionImage.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, ImageView>>) t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setActionImage(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setActionImage(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );*/
        // Add the actionStyle
        /*TableColumn actionStyle = addTableColumn(new TableColumn<Action, ActionStyle>("ActionStyle"), 70, new PropertyValueFactory<Action, ActionStyle>("actionStyle"), true);
        actionStyle.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter() {
            @Override
            public String toString(Object interpolator) {
                String str = interpolator.toString();
                return str.substring(str.indexOf(".") + 1);
            }

            @Override
            public ActionStyle fromString(String string) {
                return ActionStyle.getWrappedActionStyle(string);
            }
        }));
        actionStyle.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, ActionStyle>>) t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setActionStyle(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setActionStyle(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );*/
        TableColumn actionStyle = addTableColumn(new TableColumn<Action, String>("ActionStyle"), 110, new PropertyValueFactory<Action, String>("actionStyle"), true);
        actionStyle.setCellFactory(ComboBoxTableCell.forTableColumn(ActionStyle.values()));
        actionStyle.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, ActionStyle>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setActionStyle(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setActionStyle(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );

        // Re-setups the table
        setupTableView.getColumns().clear();
        setupTableView.getItems().clear();
        setupTableView.getColumns().addAll(actionName, actionImage, keyCode, ctrlMask, shiftMask, altMask, actionTier, actionStyle);
        setupTableView.setItems(observableListData);
    }

    /**
     * Constructs the action tier table view
     */
    private static void constructActionTierTableView() {
        ObservableList<ActionTier> observableListData = FXCollections.observableArrayList(getSavedData().getCachedActionTiers());

        actionTierTableView.setEditable(true);
        actionTierTableView.setFocusTraversable(false);
        actionTierTableView.getSelectionModel().selectedItemProperty().addListener((obs) -> actionTierTableView.requestFocus());

        // Add the id
        TableColumn id = addTableColumn(new TableColumn<ActionTier, Integer>("ID"), null, new PropertyValueFactory<ActionTier, Integer>("id"), false);
        id.setCellFactory(
                TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        id.setStyle("-fx-align: center");
        /*id.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, String>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setActionName(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setActionName(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );*/

        // Add the tier name
        TableColumn tierName = addTableColumn(new TableColumn<ActionTier, String>("Name"), null, new PropertyValueFactory<ActionTier, String>("tierName"), false);
        tierName.setCellFactory(
                TextFieldTableCell.forTableColumn());
        tierName.setStyle("-fx-align: center");

        // Add the ability border color
        TableColumn borderColor = addTableColumn(new TableColumn<ActionTier, String>("Border Color (HEX)"), null, new PropertyValueFactory<ActionTier, String>("abilityBorder"), false);
        borderColor.setCellFactory(
                TextFieldTableCell.forTableColumn());
        borderColor.setStyle("-fx-align: center");

        TableColumn colorPicker = addTableColumn(new TableColumn<ActionTier, ColorPicker>("Border Color"), null, new PropertyValueFactory<ActionTier, ColorPicker>("colorPicker"), false);
        /*borderColor.setCellFactory(
                TextFieldTableCell.forTableColumn());*/


        borderColor.setStyle("-fx-align: center");

        /*borderColor.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, String>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setActionName(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setActionName(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );*/

        // Re-setups the table
        actionTierTableView.getColumns().clear();
        actionTierTableView.getItems().clear();
        actionTierTableView.getColumns().addAll(id, tierName, /*borderColor,*/ colorPicker);
        actionTierTableView.setItems(observableListData);
    }

    /**
     * Constructs the action tier table
     */
    private static void constructActionStyleTableView() {
        ObservableList<ActionStyle> observableListData = FXCollections.observableArrayList(getSavedData().getCachedActionStyles());

        // Resizes the icons to show for the setup screen
        /*observableListData.forEach(p -> {
            p.getActionImage().setFitWidth(30);
            p.getActionImage().setFitHeight(30);
        });*/

        actionStyleTableView.setEditable(true);
        actionStyleTableView.setFocusTraversable(false);
        actionStyleTableView.getSelectionModel().selectedItemProperty().addListener((obs) -> actionStyleTableView.requestFocus());

        // Add the id
        TableColumn id = addTableColumn(new TableColumn<ActionStyle, Integer>("ID"), null, new PropertyValueFactory<ActionStyle, Integer>("id"), false);
        id.setCellFactory(
                TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        id.setStyle("-fx-align: center");
        /*id.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, String>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setActionName(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setActionName(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );*/

        // Add the tier name
        TableColumn styleName = addTableColumn(new TableColumn<ActionStyle, String>("Name"), 100, new PropertyValueFactory<ActionStyle, String>("styleName"), false);
        styleName.setMinWidth(100);
        styleName.setCellFactory(
                TextFieldTableCell.forTableColumn());
        styleName.setStyle("-fx-align: center");
        /*borderColor.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, String>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setActionName(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setActionName(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );*/

        // Re-setups the table
        actionStyleTableView.getColumns().clear();
        actionStyleTableView.getItems().clear();
        actionStyleTableView.getColumns().addAll(id, styleName);
        actionStyleTableView.setItems(observableListData);
    }

    /**
     * Opens the action tier screen
     */
    private static void openActionTierScreen() {
        Stage actionTierStage = new Stage();

        Scene scene = new Scene(new Group());
        scene.getStylesheets().add("utils/dark-theme.css");
        actionTierStage.setTitle("ActionTier - List");
        actionTierStage.setWidth(600);
        actionTierStage.setHeight(565);

        final HBox topPanel = new HBox();
        final Label searchLabel = new Label("Search: ");
        final TextField searchBar = new TextField();
        searchBar.setPrefWidth(412);
        searchBar.setFocusTraversable(false);
        final Button searchButton = new Button("Search", getMenuIcon("search.png"));
        searchButton.setFocusTraversable(false);
        searchButton.setDisable(true);
        searchBar.setDisable(true);

        refreshTable();

        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);

        // Add all components to the top panel
        topPanel.getChildren().addAll(searchLabel, sep, searchBar, searchButton);
        topPanel.setSpacing(10);
        topPanel.setPadding(new Insets(3, 0, 3, 0));

        constructActionTierTableView();

        HBox pane = new HBox();
        pane.setSpacing(10);

        // Action adding button
        final Button addButton = new Button("Add Tier", Main.getMenuIcon("plus.png"));
        addButton.setFocusTraversable(false);
        addButton.setDisable(true);
        Main.addButtonAction(addButton, Main::processAddButtonAction);

        // Action removing button
        final Button removeButton = new Button("Remove Tier", getMenuIcon("remove.png"));
        removeButton.setFocusTraversable(false);
        removeButton.setDisable(true);
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.DELETE),
                removeButton::fire
        );
        addButtonAction(removeButton, () ->
                showConfirmationDialog(
                        "Are you sure you want to delete: " + actionTierTableView.getSelectionModel().getSelectedItem().name() + "?",
                        actionTierStage,
                        Main::processRemoveButtonAction));

        // Refresh list button
        final Button refreshButton = new Button("Refresh", getMenuIcon("refresh.png"));
        refreshButton.setFocusTraversable(false);
        addButtonAction(refreshButton, Main::refreshTable);

        // Copy to clipboard button
        final Button copyToClipboardButton = new Button("_Copy to Clipboard", getMenuIcon("copy.png"));
        copyToClipboardButton.setFocusTraversable(false);
        copyToClipboardButton.setMnemonicParsing(true);
        addButtonAction(copyToClipboardButton, ()-> {
            if (actionTierTableView.getSelectionModel().isEmpty()) {
                showErrorDialog("Select a row before copying!", actionTierStage, () -> {
                });
                return;
            }
            String enumName = actionTierTableView.getSelectionModel().getSelectedItem().name();
            StringSelection stringSelection = new StringSelection(enumName);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            showInformationDialog("Copy to Clipboard", "You just copied the ActionTier called: "+enumName, "Now paste it on the Action you desire on the column called ActionTier!", actionTierStage);
        });
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN),
                copyToClipboardButton::fire
        );

        // Change action icon button
        Button changeColorButton = new Button("Set Color", getMenuIcon("color.png"));
        changeColorButton.setFocusTraversable(false);
        changeColorButton.setDisable(true);
        addButtonAction(changeColorButton, ()-> {
            if (actionTierTableView.getSelectionModel().isEmpty()) {
                showErrorDialog("Select a row before copying!", actionTierStage, () -> {
                });
                return;
            }
            final ColorPicker colorPicker = new ColorPicker(javafx.scene.paint.Color.web(actionTierTableView.getSelectionModel().getSelectedItem().getAbilityBorder()));

            Stage bike = new Stage();
            Scene cpScene = new Scene(new HBox(20), 400, 100);
            cpScene.getStylesheets().add("utils/dark-theme.css");
            HBox box = (HBox) cpScene.getRoot();
            box.setPadding(new Insets(5, 5, 5, 5));

            box.getChildren().addAll(colorPicker);
            bike.setScene(cpScene);
            bike.show();
            colorPicker.setOnAction((EventHandler) event -> {
                actionTierTableView.getSelectionModel().getSelectedItem().setAbilityBorder(colorPicker.getValue().toString());
                Optional<ActionTier> optAct = Main.cachedActionTiers.stream().filter(p-> p == actionTierTableView.getSelectionModel().getSelectedItem()).findFirst();
                if (optAct.isPresent()) {
                    Main.cachedActionTiers.get(Main.cachedActionTiers.indexOf(optAct.get())).setAbilityBorder(Integer.toHexString(colorPicker.getValue().hashCode()));
                    getSavedData().setCachedActionTiers(cachedActionTiers);
                }
                refreshTable();
            });

        });

        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);

        Separator separator3 = new Separator();
        separator3.setOrientation(Orientation.VERTICAL);

        HBox hBox = new HBox();

        TitledPane tp = new TitledPane();
        tp.setContent(pane);
        tp.setText("Options");
        tp.setExpanded(true);
        tp.setFocusTraversable(false);

        hBox.getChildren().add(tp);

        // Add all components to the pane
        pane.getChildren().addAll(addButton, removeButton, separator2, copyToClipboardButton, changeColorButton, separator3, refreshButton);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        //Vertical separator
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        vbox.getChildren().addAll(topPanel, actionTierTableView, separator, hBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        actionTierStage.setAlwaysOnTop(true);
        actionTierStage.setResizable(false);
        actionTierStage.setScene(scene);
        actionTierStage.show();

        actionTierTableView.setMaxWidth(576);

        tp.setPrefWidth(576);

        centerScreen(actionTierStage);

        addCloseEventHandler(actionTierStage, false);

        refreshTable();
    }

    /**
     * Opens the settings screen
     */
    private static void openSettingsScreen() {
        Stage settingsStage = new Stage();

        TabPane tabPane = new TabPane();
        Tab tabGeneral = new Tab("General");
        Tab tabAccount = new Tab("Account");
        tabAccount.setDisable(true);
        tabPane.getTabs().addAll(tabGeneral, tabAccount);

        // Avoid all tabs from being closed
        tabPane.getTabs().forEach(tab-> tab.setClosable(false));

        Scene settingsScene = new Scene(new VBox(tabPane));
        settingsScene.getStylesheets().add("utils/dark-theme.css");

        //final VBox panel = new VBox();
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(18, 5, 5, 45));

        List<ActionStyle> resultsWithoutNone = Arrays.stream(ActionStyle.values()).filter(p -> p != ActionStyle.NONE).collect(Collectors.toList());
        final ObservableList<ActionStyle> options = FXCollections.observableArrayList(resultsWithoutNone);
        final ComboBox cbCombatStyle = new ComboBox(options);
        grid.add(new Label("Combat Style: "), 0, 0);
        cbCombatStyle.setValue(getSavedData().getActionStyle());
        grid.add(cbCombatStyle, 1,0);

        // Add listener to the comboBox
        cbCombatStyle.valueProperty().addListener((ChangeListener<ActionStyle>) (observable, oldValue, newValue) -> {
            System.out.println(observable);
            System.out.println(oldValue);
            System.out.println(newValue);
            actionStyle = newValue;
            getSavedData().setActionStyle(newValue);
        });

        grid.add(new Separator(), 0, 1);

        final CheckBox cbAbility = new CheckBox();
        grid.add(new Label("Show action name: "), 0, 2);
        grid.add(cbAbility, 1, 2);
        cbAbility.setSelected(showActionName);

        // Add's listener to the comboBox
        cbAbility.selectedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(observable);
            System.out.println(oldValue);
            System.out.println(newValue);
            showActionName = newValue;
            getSavedData().setShowActionName(newValue);
        });

        final CheckBox cbBorder = new CheckBox();
        grid.add(new Label("Show action borders: "), 0, 3);
        grid.add(cbBorder, 1, 3);
        cbBorder.setSelected(showAbilityBorder);

        cbBorder.selectedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(observable);
            System.out.println(oldValue);
            System.out.println(newValue);
            showAbilityBorder = newValue;
            getSavedData().setShowAbilityBorder(newValue);
        });

        tabGeneral.setContent(grid);

        settingsStage.setTitle("Settings");
        settingsStage.setWidth(300);
        settingsStage.setHeight(170);

        ((VBox) settingsScene.getRoot()).getChildren().addAll(grid);

        settingsStage.setAlwaysOnTop(true);
        settingsStage.setResizable(false);
        settingsStage.setScene(settingsScene);

        centerScreen(settingsStage);

        settingsStage.show();
    }

    /**
     * Opens the setup screen
     */
    private static void openSetupScreen() {
        Stage setupStage = new Stage();

        Scene scene = new Scene(new Group());
        scene.getStylesheets().add("utils/dark-theme.css");
        setupStage.setTitle("Action Management");
        setupStage.setWidth(600);
        setupStage.setHeight(565);

        final HBox topPanel = new HBox();
        final Label searchLabel = new Label("Search: ");
        final TextField searchBar = new TextField();
        searchBar.setPrefWidth(412);
        searchBar.setFocusTraversable(false);
        final Button searchButton = new Button("Search", getMenuIcon("search.png"));
        searchButton.setFocusTraversable(false);
        searchButton.setDisable(true);
        searchBar.setDisable(true);

        refreshTable();

        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);

        // Add all components to the top panel
        topPanel.getChildren().addAll(searchLabel, sep, searchBar, searchButton);
        topPanel.setSpacing(10);
        topPanel.setPadding(new Insets(3, 0, 3, 0));

        constructSetupTableView(setupStage);

        HBox pane = new HBox();
        pane.setSpacing(10);

        // Action adding button
        Button addButton = new Button("Add Action", Main.getMenuIcon("plus.png"));
        addButton.setFocusTraversable(false);
        Main.addButtonAction(addButton, Main::processAddButtonAction);

        // Action removing button
        Button removeButton = new Button("Remove Action", getMenuIcon("remove.png"));
        removeButton.setFocusTraversable(false);
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.DELETE),
                removeButton::fire
        );
        addButtonAction(removeButton, () ->
                showConfirmationDialog(
                        "Are you sure you want to delete: " + setupTableView.getSelectionModel().getSelectedItem().getActionName() + "?",
                        setupStage,
                        Main::processRemoveButtonAction));

        // Refresh list button
        Button refreshButton = new Button("Refresh", getMenuIcon("refresh.png"));
        refreshButton.setFocusTraversable(false);
        addButtonAction(refreshButton, Main::refreshTable);

        // Change action icon button
        Button changeIconButton = new Button("Set Icon", getMenuIcon("image.png"));
        changeIconButton.setFocusTraversable(false);
        addButtonAction(changeIconButton, ()-> changeAbilityIcon(setupTableView.getSelectionModel().getSelectedItem(), setupStage));

        // Change key bind
        Button changeKeyBindButton = new Button("Set Keybind", getMenuIcon("keybind.png"));
        changeKeyBindButton.setFocusTraversable(false);
        addButtonAction(changeKeyBindButton, ()-> changeKeyBind(setupTableView.getSelectionModel().getSelectedItem(), setupStage));

        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);

        Separator separator3 = new Separator();
        separator3.setOrientation(Orientation.VERTICAL);

        HBox hBox = new HBox();

        TitledPane tp = new TitledPane();
        tp.setContent(pane);
        tp.setText("Options");
        tp.setExpanded(true);
        tp.setFocusTraversable(false);

        hBox.getChildren().add(tp);

        // Add all components to the pane
        pane.getChildren().addAll(addButton, removeButton, separator2, changeKeyBindButton, changeIconButton, separator3, refreshButton);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        //Vertical separator
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        vbox.getChildren().addAll(topPanel, setupTableView, separator, hBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        setupStage.setAlwaysOnTop(true);
        setupStage.setResizable(false);
        setupStage.setScene(scene);
        setupStage.show();

        setupTableView.setMaxWidth(576);

        tp.setPrefWidth(576);

        centerScreen(setupStage);

        addCloseEventHandler(setupStage, false, ()-> {
            stopResume.setDisable(false);
        });

        refreshTable();
    }

    /**
     * Opens the action tier screen
     */
    private static void openActionStyleScreen() {
        Stage actionStyleStage = new Stage();

        Scene scene = new Scene(new Group());
        scene.getStylesheets().add("utils/dark-theme.css");
        actionStyleStage.setTitle("ActionStyle - List");
        actionStyleStage.setWidth(600);
        actionStyleStage.setHeight(565);

        final HBox topPanel = new HBox();
        final Label searchLabel = new Label("Search: ");
        final TextField searchBar = new TextField();
        searchBar.setPrefWidth(412);
        searchBar.setFocusTraversable(false);
        final Button searchButton = new Button("Search", getMenuIcon("search.png"));
        searchButton.setFocusTraversable(false);
        searchButton.setDisable(true);
        searchBar.setDisable(true);

        refreshTable();

        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);

        // Add all components to the top panel
        topPanel.getChildren().addAll(searchLabel, sep, searchBar, searchButton);
        topPanel.setSpacing(10);
        topPanel.setPadding(new Insets(3, 0, 3, 0));

        constructActionStyleTableView();

        HBox pane = new HBox();
        pane.setSpacing(10);

        // Action adding button
        Button addButton = new Button("Add Style", Main.getMenuIcon("plus.png"));
        addButton.setFocusTraversable(false);
        addButton.setDisable(true);
        Main.addButtonAction(addButton, Main::processAddButtonAction);

        // Action removing button
        Button removeButton = new Button("Remove Style", getMenuIcon("remove.png"));
        removeButton.setFocusTraversable(false);
        removeButton.setDisable(true);
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.DELETE),
                removeButton::fire
        );
        addButtonAction(removeButton, () ->
                showConfirmationDialog(
                        "Are you sure you want to delete: " + actionStyleTableView.getSelectionModel().getSelectedItem().name() + "?",
                        actionStyleStage,
                        Main::processRemoveButtonAction));

        // Refresh list button
        Button refreshButton = new Button("Refresh", getMenuIcon("refresh.png"));
        refreshButton.setFocusTraversable(false);
        addButtonAction(refreshButton, Main::refreshTable);

        // Copy to clipboard button
        final Button copyToClipboardButton = new Button("_Copy to Clipboard", getMenuIcon("copy.png"));
        copyToClipboardButton.setFocusTraversable(false);
        copyToClipboardButton.setMnemonicParsing(true);
        addButtonAction(copyToClipboardButton, ()-> {
            if (actionStyleTableView.getSelectionModel().isEmpty()) {
                showErrorDialog("Select a row before copying!", actionStyleStage, () -> {
                });
                return;
            }
            String enumName = actionStyleTableView.getSelectionModel().getSelectedItem().name();
            StringSelection stringSelection = new StringSelection(enumName);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            showInformationDialog("Copy to Clipboard", "You just copied the ActionStyle called: "+enumName, "Now paste it on the Action you desire on the column called ActionStyle!", actionStyleStage);
        });
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN),
                copyToClipboardButton::fire
        );

        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);

        Separator separator3 = new Separator();
        separator3.setOrientation(Orientation.VERTICAL);

        HBox hBox = new HBox();

        TitledPane tp = new TitledPane();
        tp.setContent(pane);
        tp.setText("Options");
        tp.setExpanded(true);
        tp.setFocusTraversable(false);

        hBox.getChildren().add(tp);

        // Add all components to the pane
        pane.getChildren().addAll(addButton, removeButton, separator2, copyToClipboardButton, separator3, refreshButton);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        //Vertical separator
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        vbox.getChildren().addAll(topPanel, actionStyleTableView, separator, hBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        actionStyleStage.setAlwaysOnTop(true);
        actionStyleStage.setResizable(false);
        actionStyleStage.setScene(scene);
        actionStyleStage.show();

        actionStyleTableView.setMaxWidth(576);

        tp.setPrefWidth(576);

        centerScreen(actionStyleStage);

        addCloseEventHandler(actionStyleStage, false);

        refreshTable();
    }

    /**
     * Shows a confirmation dialog pane
     * @param contentText The description about the panel
     * @param owner Which stage was this called from (the owner)
     * @param exec The execution when clicking on OK button
     */
    private static void showConfirmationDialog(String contentText, Stage owner, Runnable exec) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.initOwner(owner);
        if (contentText != null) {
            a.setContentText(contentText);
        }
        a.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> exec.run());
    }

    /**
     * Shows a confirmation dialog pane
     * @param contentText The description about the panel
     * @param headerText The headerText
     * @param owner Which stage was this called from (the owner)
     * @param exec The execution when clicking on OK button
     */
    private static void showLinkToPatreonDialog(String title, String headerText, String contentText, Stage owner, Runnable exec) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.initOwner(owner);
        a.setTitle(title);
        a.setHeaderText(headerText);
        a.setContentText(contentText);
        a.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(response -> exec.run());
    }

    /**
     * Show errro dialog
     * @param contentText The description about the panel
     * @param owner Which stage was this called from (the owner)
     * @param exec The execution when clicking on OK button
     */
    private static void showErrorDialog(String contentText, Stage owner, Runnable exec) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.initOwner(owner);
        if (contentText != null) {
            a.setContentText(contentText);
        }
        a.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> exec.run());
    }

    /**
     * Shows an information dialog
     * @param title The dialog title
     * @param headerText The header text
     * @param contentText The contentText
     * @param owner The owner who called
     */
    private static void showInformationDialog(String title, String headerText, String contentText, Stage owner) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.initOwner(owner);
        a.setTitle(title);
        a.setHeaderText(headerText);
        a.setContentText(contentText);

        a.showAndWait();
    }


    /**
     * Processes the add button action
     */
    private static void processAddButtonAction() {
        Action newAction = new Action(/*configTable,*/ generateRandomActionName(), 0, false, false, false, ActionTier.BASIC_ABILITY, RESOURCES_PATH + "placeholder.png", ActionStyle.NONE);

        // Lets create the action image so it updates on scren
        newAction.setActionImage(new ImageView(new Image(new File(newAction.getIconPath()).toURI().toString())));

        // Resizes the icons to show for the setup screen

        newAction.getActionImage().setFitWidth(30);
        newAction.getActionImage().setFitHeight(30);

        setupTableView.getItems().add(newAction);
        cachedActions.add(newAction);

        getSavedData().setCachedActions(cachedActions);

        setupTableView.scrollTo(newAction);

        setupTableView.requestFocus();

        refreshTable();
        System.out.println("processAddButtonAction("+newAction.getActionName()+")");
    }

    /**
     * Processes the remove button action
     */
    private static void processRemoveButtonAction() {
        if (setupTableView.getSelectionModel() == null || setupTableView.getSelectionModel().isEmpty()) {
            System.out.println("Please select a row before deleting!");
            return;
        }
        Action selectedRow = setupTableView.getSelectionModel().getSelectedItem();

        // Just a normal check to avoid things from being deleted while not present on our list
        Optional<Action> optAction = cachedActions.stream().filter(a-> a.getActionName().toLowerCase().equals(selectedRow.getActionName().toLowerCase())).findFirst();
        if (!optAction.isPresent()) {
            System.out.println("Data doesn't exist, nothing is being deleted!");
            return;
        }

        System.out.println("removed='" + selectedRow.getActionName() + "'");
        setupTableView.getItems().remove(setupTableView.getSelectionModel().getSelectedItem());

        cachedActions.removeIf(p -> p.getActionName().toLowerCase().equals(selectedRow.getActionName().toLowerCase()));

        getSavedData().setCachedActions(cachedActions);

        refreshTable();
    }

    /**
     * Add a close event handler to a stage
     *
     * @param stage       The stage
     * @param exitProgram {@code True} if program should be finalized - {@code False} if only scene
     */
    private static void addCloseEventHandler(Stage stage, boolean exitProgram) {
        stage.setOnCloseRequest(event -> {
            System.out.println("Stage " + stage + " is closing");
            stage.close();
            if (exitProgram) {
                System.runFinalization();
                System.exit(0);
            }
        });
    }

    /**
     * Add a close event handler to a stage
     *
     * @param stage       The stage
     * @param exitProgram {@code True} if program should be finalized - {@code False} if only scene
     * @param runnable {@code null} if you dont need any runnable | {@code ()-> Lambda function to be ran}
     */
    private static void addCloseEventHandler(Stage stage, boolean exitProgram, Runnable runnable) {
        stage.setOnCloseRequest(event -> {
            System.out.println("Stage " + stage + " is closing");
            stage.close();
            if (runnable != null)
                runnable.run();
            if (exitProgram) {
                System.runFinalization();
                System.exit(0);
            }
        });
    }



    /**
     * Gets a menu icon
     *
     * @param iconName The iconName
     * @return {@code ImageView} if true {@code null} if not exists
     */
    public static ImageView getMenuIcon(String iconName) {
        ImageView img = new ImageView(ICONS_PATH + "/" + iconName);
        img.setFitWidth(15);
        img.setFitHeight(15);
        return img;
    }

    /**
     * Refresh table
     */
    private static void refreshTable() {
        actionTierTableView.refresh();
        actionStyleTableView.refresh();
        setupTableView.refresh();
        setupTableView.getItems().forEach(p-> {
            p.setActionImage(new ImageView(new Image(new File(p.getIconPath()).toURI().toString())));

            // Resize the images
            p.getActionImage().setFitWidth(30);
            p.getActionImage().setFitHeight(30);
        });
        System.out.println("Table refreshed");
    }

    /**
     * Change the action icon
     */
    private static void changeAbilityIcon(Action action, Stage stage) {
        if (action == null) {
            System.out.println("Action isn't selected");
            return;
        }
        Optional<Action> optAction = cachedActions.stream().filter(p-> p.getActionName().toLowerCase().equals(action.getActionName().toLowerCase())).findFirst();
        if (!optAction.isPresent()) {
            System.out.println("action doesn't exist in database");
        } else {
            Action selectedAction = optAction.get();
            FileChooser fileChooser = new FileChooser();
            FileManager.configureFileChooser(fileChooser, "Change Action Icon");
            fileChooser.setTitle("Open Resource File");

            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                //FileManager.openFile(file);
                String cwd = System.getProperty("user.dir");
                selectedAction.setIconPath(new File(cwd).toURI().relativize(file.toURI()).getPath());

                cachedActions.set(cachedActions.indexOf(selectedAction), selectedAction);

                getSavedData().setCachedActions(cachedActions);

                refreshTable();
            }
            System.out.println("changeAbilityIcon(" + action.getActionName() + ")");
        }
    }

    private static Stage changingKeyBind;
    /**
     * Changes the key bind
     */
    private static void changeKeyBind(Action action, Stage called) {
        changingKeyBind = new Stage();

        if (action == null || setupTableView.getSelectionModel().isEmpty()) {
            System.out.println("Action isn't selected");
            showErrorDialog("Select a row before setting the action keybind!", called, () -> {
            });
            return;
        }
        Optional<Action> optAction = cachedActions.stream().filter(p-> p.getActionName().toLowerCase().equals(action.getActionName().toLowerCase())).findFirst();
        if (!optAction.isPresent()) {
            System.out.println("action doesn't exist in database");
        }

        System.out.println("changeKeyBind("+action.getActionName()+")");

        Group group = new Group();
        Scene keyBindScene = new Scene(group, 430, 50);

        Label pressKey = new Label("Press any key you'd like to bind to your action!");
        pressKey.setStyle("-fx-font-size:20px;" +
                "-fx-font-color:blue;");
        AnchorPane.setLeftAnchor(pressKey, 0.0);
        AnchorPane.setRightAnchor(pressKey, 0.0);
        pressKey.setAlignment(Pos.CENTER);
        pressKey.setMaxWidth(Double.MAX_VALUE);
        pressKey.setPadding(new Insets(10, 10, 10 , 10));

        group.getChildren().add(pressKey);

        changingKeyBind.setTitle("Keybinding");
        changingKeyBind.setScene(keyBindScene);
        changingKeyBind.setAlwaysOnTop(true);
        changingKeyBind.setResizable(false);
        changingKeyBind.centerOnScreen();
        changingKeyBind.sizeToScene();
        changingKeyBind.initStyle(StageStyle.UTILITY);
        changingKeyBind.show();
        centerScreen(changingKeyBind);

        // To avoid bug related to setting keybind
        changingKeyBind.setOnCloseRequest(event -> {
            changingKeyBind.close();
            setActionStatus(ActionStatus.IDLE);
        });

        //setKeyBindMode = true;
        setActionStatus(ActionStatus.SETTING_KEYBIND);
    }



    /**
     * Returns a random name for the action adding name
     *
     * @return The ability name
     */
    private static String generateRandomActionName() {
        String[] prefixes = {"Pretty", "Ugly", "Incredible", "Amazing", "Delicious", "Matty's", "Crazy", "Special", "Great", "Splendorous", "Dirty", "Poopy"};
        Random random = new Random();
        int randomInteger = random.nextInt(prefixes.length);
        return prefixes[randomInteger] + " Action";
    }

    /**
     * Create a table column
     *
     * @param column       - the table column
     * @param preferredSize The preferred size {@code null} for automatic
     * @param prop         - The properties
     * @return The generated TableColumn
     */
    private static TableColumn addTableColumn(TableColumn column, Integer preferredSize, PropertyValueFactory prop, boolean isEditable) {
        if (preferredSize != null) {
            column.setMinWidth(preferredSize);
        }
        column.setCellValueFactory(prop);
        column.setEditable(isEditable);
        return column;
    }

    private static TableColumn<Action, String> addTableColumn2(TableColumn column, Integer preferredSize, PropertyValueFactory prop, boolean isEditable) {
        if (preferredSize != null) {
            column.setMinWidth(preferredSize);
        }
        column.setCellValueFactory(prop);
        column.setEditable(isEditable);
        return column;
    }

    /**
     * The bootstrapper
     *
     * @param args Program arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }

    /**
     * Adds a menuItem action when clicking on them
     *
     * @param menuItem The menuItem we are clicking
     * @param action   The action that should it do
     */
    private static void addMenuItemAction(MenuItem menuItem, Runnable action) {
        menuItem.setOnAction(event -> {
            action.run();
            System.out.println("clicked at " + menuItem.getText());
        });
    }

    /**
     * Adds a new button action when clicking on it
     *
     * @param button The button we are clicking
     * @param action The action that should it do
     */
    public static void addButtonAction(Button button, Runnable action) {
        button.setOnAction(event -> {
            action.run();
            System.out.println("Clicked at button: " + button.getText());
        });
    }

    /**
     * Adds a menu action when clicking on them
     *
     * @param menu   The menuItem we are clicking
     * @param action The action that should it do
     */
    private static void addMenuAction(Menu menu, Runnable action) {
        final MenuItem menuItem = new MenuItem();
        menu.getItems().add(menuItem);

        menu.addEventHandler(Menu.ON_SHOWN, event -> menu.hide());
        menu.addEventHandler(Menu.ON_SHOWING, event -> {
            menu.fire();
            action.run();
            System.out.println("clicked at " + menu.getText());
            event.consume();
        });
    }

    /**
     * Represents the control down
     *
     * @param e The key event
     * @return {@code True} if yes {@code False} if not
     */
    private boolean isControlDown(final NativeKeyEvent e) {
        return (e.getModifiers() & CTRL_MASK) != 0;
    }

    /**
     * Represents the shift down
     *
     * @param e The key event
     * @return {@code True} if yes {@code False} if not
     */
    private boolean isShiftDown(final NativeKeyEvent e) {
        return (e.getModifiers() & SHIFT_MASK) != 0;
    }

    /**
     * Represents the alt down
     *
     * @param e The key event
     * @return {@code True} if yes {@code False} if not
     */
    private boolean isAltDown(final NativeKeyEvent e) {
        return (e.getModifiers() & ALT_MASK) != 0;
    }

    /**
     * Toggles the idle mode
     */
    private static void toggleIdleMode() {
        setActionStatus(getActionStatus() == ActionStatus.IDLE ? ActionStatus.LOGGING : ActionStatus.IDLE);

        lastKeyPressed = new LastKeyPressed(LocalTime.now(), NativeKeyEvent.VC_F11);
        updateTitle(mainStage);
        if (getActionStatus() == ActionStatus.LOGGING)
            stopResume.setText(stopResumeLabel[1][0]);
        else
            stopResume.setText(stopResumeLabel[2][0]);
        System.out.println("Status: "+getActionStatus().name()+"!");
    }

    private static void stopLoggingMode() {
            if (getActionStatus() != ActionStatus.LOGGING)
                return;
            setActionStatus(ActionStatus.IDLE);
            stopResume.setDisable(true);

            lastKeyPressed = new LastKeyPressed(LocalTime.now(), NativeKeyEvent.VC_F11);
            updateTitle(mainStage);
            if (getActionStatus() == ActionStatus.LOGGING)
                stopResume.setText(stopResumeLabel[1][0]);
            else
                stopResume.setText(stopResumeLabel[2][0]);
            System.out.println("Status: "+getActionStatus().name()+"!");
    }

    /**
     * Used to toggle chat mode
     */
    public static void toggleChattingMode() {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int key = nativeKeyEvent.getKeyCode();

        Platform.runLater(() -> {
            // Check if player is chatting
            if (key == NativeKeyEvent.VC_ENTER || key == NativeKeyEvent.VC_TAB) {
                if (getActionStatus() == ActionStatus.LOGGING) {
                    setActionStatus(ActionStatus.CHATTING);
                    System.out.println("Player entered on chatting mode");
                } else if (getActionStatus() == ActionStatus.CHATTING) {
                    setActionStatus(ActionStatus.LOGGING);
                    System.out.println("Player ended chatting mode! Logging back again");
                }
            }

            // Checks if player clicked on ESC button
            if (key == NativeKeyEvent.VC_ESCAPE) {
                if (getActionStatus() == ActionStatus.CHATTING) {
                    setActionStatus(ActionStatus.LOGGING);
                    System.out.println("Cancelled chat mode");
                }
            }

            // Checks if player was in CHAT MODE
            if (getActionStatus() == ActionStatus.CHATTING) {
                return;
            }

            // Checks if key was already pressed
            if (lastKeyPressed != null && nativeKeyEvent.getKeyCode() == lastKeyPressed.getKeyCode()) {
                System.out.println("key already pressed");
                return;
            }

            // Checks if player is in idle mode
            if (/*!isCombatMode && !setKeyBindMode*/getActionStatus().equals(ActionStatus.IDLE)
                    && nativeKeyEvent.getKeyCode() != NativeKeyEvent.VC_F12) {
                System.out.println("Player is in idle mode");
                return;
            }
            if (getActionStatus().equals(ActionStatus.SETTING_KEYBIND)) { // Choosing a new keybind
                System.out.println("The key you selected was :"+nativeKeyEvent.getKeyCode());

                // Sets the new key bind to the selected row item
                Action selectedAction = setupTableView.getSelectionModel().getSelectedItem();
                selectedAction.setPressedKey(nativeKeyEvent.getKeyCode());
                Optional<Action> optAction = cachedActions.stream().filter(action -> action == selectedAction).findFirst();
                optAction.ifPresent(action -> cachedActions.set(cachedActions.indexOf(optAction.get()), selectedAction));

                // If any other action is using the key, remove it
                /*List<Action> keysAlreadyUsedList = cachedActions.stream().filter(act -> act.getPressedKey() == nativeKeyEvent.getKeyCode() && act != selectedAction).collect(Collectors.toList());
                keysAlreadyUsedList.forEach(action -> {
                    showInformationDialog("Duplicated Keybind", "Key was already bound to "+action.getActionName().toUpperCase()+"!", "The action: "+action.getActionName().toUpperCase()+" got it's key unbound!\nPlease select a new key bind to it!", changingKeyBind);
                    cachedActions.get(cachedActions.indexOf(action)).setPressedKey(-1);
                    setupTableView.getFocusModel().focus(cachedActions.indexOf(action));
                });*/

                getSavedData().setCachedActions(cachedActions);

                refreshTable();

                //setKeyBindMode = false;
                setActionStatus(ActionStatus.IDLE);
                changingKeyBind.close();
                return;
            }
            for (Action action : cachedActions) {
                if (action.getActionStyle() != actionStyle
                        && action.getActionStyle() != ActionStyle.NONE)
                    continue;

                // If the pressed key exists in our actions array
                if ((isControlDown(nativeKeyEvent)) && (action.getPressedKey() == key)) { // Ctrl pressed
                    if (!action.isCtrlPressed())
                        continue;
                    processKeyAction(action, nativeKeyEvent);
                } else if ((isShiftDown(nativeKeyEvent)) && (action.getPressedKey() == key)) { // Shift pressed
                    if (!action.isShiftPressed())
                        continue;
                    processKeyAction(action, nativeKeyEvent);
                } else if ((isAltDown(nativeKeyEvent)) && (action.getPressedKey() == key)) { // Alt pressed
                    if (!action.isAltPressed())
                        continue;
                    processKeyAction(action, nativeKeyEvent);
                } else if (action.getPressedKey() == key && (!action.isCtrlPressed() && !action.isAltPressed() && !action.isShiftPressed())) {
                    processKeyAction(action, nativeKeyEvent);
                }
            }

            // Prints our current actions cache
            List<String> actionsCache = new ArrayList<>();
            Main.actions.forEach(action -> {
                actionsCache.add(action.getActionName());
            });
            System.out.println(actionsCache.toString());
        });
    }

    /**
     * Processes the key action
     *
     * @param action The key action
     */
    private static void processKeyAction(Action action, NativeKeyEvent nativeKeyEvent) {
        int key = nativeKeyEvent.getKeyCode();

        lastKeyPressed = new LastKeyPressed(LocalTime.now(), nativeKeyEvent.getKeyCode());

        // Adds the action to the screen
        addActionToScreen(action, nativeKeyEvent);

        // Prints the key we pressed
        System.out.println("actionName='" + action.getActionName() + "', key=" + (action.isCtrlPressed() ? "CTRL+" : action.isShiftPressed() ? "Shift+" : action.isAltPressed() ? "ALT+" : "") + key + ");");
    }

    private static void updateTitle(Stage stage) {
        stage.setTitle(TITLE + " | status=" + actionStatus + " | actionsDone=" + actionsDone + " | user=Sagacity");
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    }

    /**
     * Loads the database
     */
    private static void loadDatabase() {
        try {
            File myObj = new File(SavedData.SAVINGS_PATH);
            if (myObj.createNewFile()) {
                System.out.println("Database file didn't exist, creating one...");
                SavedData newSavings = new SavedData();
                GsonUtil.save(newSavings, SavedData.SAVINGS_PATH, SavedData.class);
                savedData = newSavings;
                System.out.println("Database file created: " + myObj.getName());
            } else {
                savedData = (SavedData) GsonUtil.load(SavedData.SAVINGS_PATH, SavedData.class);
                System.out.println("Database file loaded.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}