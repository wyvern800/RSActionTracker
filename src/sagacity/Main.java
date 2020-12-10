package sagacity;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;
import objects.*;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import utils.FileManager;
import utils.FileWritter;
import utils.GsonUtil;
import utils.MasksConstants;
import javafx.scene.image.Image;

import javax.swing.text.html.Option;
import java.awt.*;
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
    private static final ActionStyle COMBAT_STYLE = ActionStyle.RANGED;

    // The actionStatus
    private static ActionStatus actionStatus = ActionStatus.IDLE;

    // The configTable
    private static TableView<Action> setupTableView;

    /**
     * Cached actions
     */
    public static List<Action> cachedActions;

    /**
     * The personal pressed actions (that is used to store session
     */
    public static List<Action> actions;

    /**
     * The scenes
     */
    private static List<Scene> scenes;


    /**
     * {@code True} if in keys should be logged, {@code False} if not
     */
    private static boolean isCombatMode = false;
    private static boolean showActionName = true;

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
     * The constructor
     */
    public Main() {
        // Initializations
        scenes = new ArrayList<>();
        cachedActions = new ArrayList<>();
        actions = new ArrayList<>();
        //cachedActions = getSavedData().getCachedActions();
        //actions = getSavedData().getActions();

        // Load all actions to our cache
        /*List<Action> tempActList = new ArrayList<>();
        for (ActionList store : ActionList.values()) {
            if (store == null)
                continue;
            tempActList.add(store.getAction());
            cachedActions.add(store.getAction());
        }*/

        // Counter
        /*List<String> tempActNames = new ArrayList<>();
        for (Action store : tempActList) {
            tempActNames.add(store.getActionName());
        }
        System.out.println("totalLoaded=" + tempActNames.size() + ", actionsList=" + Arrays.toString(tempActNames.toArray()));*/

        loadDatabase();

        cachedActions = getSavedData().getCachedActions();

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
     *
     * @param i The index we're adding to
     */
    private static void addActionToScreen(Action action, NativeKeyEvent nativeKeyEvent) {
        // Handles the keys pressing
        actions.add(action);

        if (actions.size() > MAX_SIZE) {
            actions.remove(0); // Removes the first element if list is full
            mainGridPane.getChildren().remove(0);
            ObservableList<Node> arroba = mainGridPane.getChildren();
            arroba = new SortedList<>(arroba);
            mainGridPane.getChildren().clear();
            mainGridPane.getChildren().addAll(arroba);
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
                    "-fx-border-color: " + actionToBeAdded.getActionTier().getAbilityBorder());

            actionToBeAdded.getActionImage().setFitWidth(104);
            actionGroup.getChildren().add(actionToBeAdded.getActionImage());
            actionGroup.getChildren().add(actionName);

            actionBox.getChildren().add(actionGroup);

            actionName.setVisible(showActionName);

            // Add the actionBox to the gridPane
            mainGridPane.add(actionBox, i, 0);
        }
    }

    /**
     * Updates the screen with the actions
     */
    private static void updateScreen() {
        // Loops through the personal actions and set them to screen
        /*for (int i = 0; i < actions.size(); i++) {
            addActionToScreen(i);
        }*/
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

        // create the menus
        final Menu menu = new Menu("Options", getMenuIcon("list.png"));
        final Menu configurations = new Menu("Setup", getMenuIcon("setup.png"));
        final Menu links = new Menu("Links", getMenuIcon("help.png"));
        final MenuItem help = new MenuItem("Help", getMenuIcon("config.png"));
        final MenuItem about = new MenuItem("About", getMenuIcon("config.png"));
        final MenuItem discord = new MenuItem("Discord", getMenuIcon("config.png"));
        addMenuItemAction(discord, ()-> {
            try {
                Desktop.getDesktop().browse(new URL("https://discord.gg/yaUHKTWJSJ").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
        final MenuItem purchase = new MenuItem("Purchase a License", getMenuIcon("config.png"));
        purchase.setDisable(true);
        final SeparatorMenuItem sep2 = new SeparatorMenuItem();
        links.getItems().addAll(help, discord, about, sep2, purchase);
        stopResume = new Menu(stopResumeLabel[0][0], getMenuIcon(stopResumeLabel[0][1]));
        addMenuItemAction(help, ()-> {
            try {
                Desktop.getDesktop().browse(new URL("https://github.com/wyvern800/RSActionLogger/blob/master/README.md").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
        addMenuItemAction(about, ()-> {
            System.out.println("about");
        });
        addMenuItemAction(purchase, ()-> {
            System.out.println("purchase");
        });
        /*addMenuAction(help, () -> {
            try {
                Desktop.getDesktop().browse(new URL("https://github.com/wyvern800/RSActionLogger/blob/master/README.md").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });*/
        // Combat menu
        final MenuItem toggleCombatMode = new MenuItem("Toggle Idle mode", getMenuIcon("config.png"));
        final MenuItem toggleAbilityName = new MenuItem("Toggle display ability name", getMenuIcon("config.png"));
        // add menu items to menu
        menu.getItems().addAll(toggleCombatMode, toggleAbilityName);

        addMenuItemAction(toggleCombatMode, ()-> toggleIdleMode(false));

        addMenuItemAction(toggleAbilityName, () -> {
            showActionName = !showActionName;
            System.out.println("Ability name is now " + (showActionName ? "enabled" : "disabled"));
        });

        addMenuAction(stopResume, ()-> toggleIdleMode(false));

        // Configurations menu
        final MenuItem configureAbilities = new MenuItem("Actions", getMenuIcon("click.png"));
        final MenuItem actionStyle = new MenuItem("Action Styles", getMenuIcon("config.png"));
        actionStyle.setDisable(true);
        final MenuItem actionTier = new MenuItem("Action Tiers", getMenuIcon("config.png"));
        actionTier.setDisable(true);
        addMenuItemAction(configureAbilities, ()-> {
            toggleIdleMode(true);
            openSetupScreen();
        });
        addMenuItemAction(actionStyle, Main::openActionStyles);
        addMenuItemAction(actionTier, Main::openActionTiers);
        configurations.getItems().addAll(configureAbilities, actionStyle, actionTier);

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
        Scene mainScene = new Scene(verticalBox, 1100, 137);
        // set a background color to the vertical box
        verticalBox.setStyle("-fx-background-color: #000000;");
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

        stopResume.setMnemonicParsing(true);
        mainStage.getScene().getAccelerators().put(
                new KeyCodeCombination(KeyCode.F12),
                () -> {
                    stopResume.fire();
                    toggleIdleMode(false);
                }
        );


        addCloseEventHandler(mainStage, true);
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

    private static void openActionTiers() {
        System.out.println("actionTiers");
    }

    private static void openActionStyles() {
        System.out.println("actionStyles");
    }

    //private static ObservableList<Action> observableListData;

    private static void constructTableView(Stage ownerStage) {
        //observableListData = FXCollections.observableArrayList(cachedActions);
        ObservableList<Action> observableListData = FXCollections.observableArrayList(getSavedData().getCachedActions());

        cachedActions.forEach(a-> {
            a.setActionImage(new ImageView(new Image(new File(a.getIconPath()).toURI().toString())));
            refreshTable();
        });

        // Resizes the icons to show for the setup screen
        /*observableListData.forEach(p -> {
            p.getActionImage().setFitWidth(30);
            p.getActionImage().setFitHeight(30);
        });*/

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
        TableColumn keyCode = addTableColumn(new TableColumn<Action, Integer>("KeyCode"), null, new PropertyValueFactory<Action, Integer>("pressedKey"), true);
        keyCode.setCellFactory(
                TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        keyCode.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, Integer>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setPressedKey(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setPressedKey(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );
        // Add the ctrlMask
        TableColumn ctrlMask = addTableColumn(new TableColumn<Action, Boolean>("CTRL?"), null, new PropertyValueFactory<Action, Boolean>("ctrlPressed"), true);
        ctrlMask.setCellFactory(
                TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
        ctrlMask.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, Boolean>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setCtrlPressed(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setCtrlPressed(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );
        // Add the shiftMask
        TableColumn shiftMask = addTableColumn(new TableColumn<Action, Boolean>("Shift?"), null, new PropertyValueFactory<Action, Boolean>("shiftPressed"), true);
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
        );
        /*TableColumn<Action, Boolean> shiftTableColumn = new TableColumn<>("Shift?");
        PropertyValueFactory<Action, Boolean> shiftPropertyValueFactory = new PropertyValueFactory<>("shiftPressed");
        TableColumn<Action, Boolean> shiftMask = addTableColumn(shiftTableColumn, null, shiftPropertyValueFactory, true);
        shiftMask.setCellValueFactory(shiftPropertyValueFactory);
        //shiftMask.setCellFactory(CheckBoxTableCell.forTableColumn(shiftTableColumn));
        shiftMask.setCellFactory( new Callback<TableColumn<Action,Boolean>, TableCell<Action,Boolean>>() {
            @Override
            public TableCell<Action,Boolean> call( TableColumn<Action,Boolean> param ) {
                return new CheckBoxTableCell<Action,Boolean>() {
                    {
                        setAlignment( Pos.CENTER );
                    }
                    @Override
                    public void updateItem( Boolean item, boolean empty ) {
                        if ( ! empty ) {
                            TableRow  row = getTableRow();

                            if ( row != null ) {
                                int rowNo = row.getIndex();
                                TableView.TableViewSelectionModel sm = getTableView().getSelectionModel();

                                if ( item ) sm.select( rowNo );
                                else  sm.clearSelection( rowNo );
                            }
                        }
                        super.updateItem( item, empty );
                    }
                };
            }
        } );
        shiftMask.setOnEditCommit(t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setShiftPressed(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setShiftPressed(t.getNewValue());
            }
        );*/
        // Add the altMask
        TableColumn altMask = addTableColumn(new TableColumn<Action, Boolean>("Alt?"), null, new PropertyValueFactory<Action, Boolean>("altPressed"), true);
        altMask.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
        altMask.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, Boolean>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setAltPressed(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setAltPressed(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );
        // Add the actionTier
        TableColumn actionTier = addTableColumn(new TableColumn<Action, ActionTier>("Action Tier"), null, new PropertyValueFactory<Action, ActionTier>("actionTier"), true);
        actionTier.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter() {
            @Override
            public String toString(Object interpolator) {
                String str = interpolator.toString();
                return str.substring(str.indexOf(".") + 1);
            }

            @Override
            public ActionTier fromString(String string) {
                return ActionTier.getWrappedActionTier(string);
            }
        }));
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
        TableColumn actionStyle = addTableColumn(new TableColumn<Action, ActionStyle>("Action Style"), 70, new PropertyValueFactory<Action, ActionStyle>("actionStyle"), true);

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
        );

        // Re-setups the table
        setupTableView.getColumns().clear();
        setupTableView.getItems().clear();
        setupTableView.getColumns().addAll(actionName, actionImage, keyCode, ctrlMask, shiftMask, altMask, actionTier, actionStyle);
        setupTableView.setItems(observableListData);
    }

    /**
     * Opens the setup screen
     */
    private static void openSetupScreen() {
        Stage setupStage = new Stage();

        Scene scene = new Scene(new Group());
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

        constructTableView(setupStage);

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
        Button refreshButton = new Button("Refresh List", getMenuIcon("refresh.png"));
        refreshButton.setFocusTraversable(false);
        addButtonAction(refreshButton, Main::refreshTable);

        // Change action icon button
        Button changeIconButton = new Button("Set Icon", getMenuIcon("image.png"));
        changeIconButton.setFocusTraversable(false);
        addButtonAction(changeIconButton, ()-> changeAbilityIcon(setupTableView.getSelectionModel().getSelectedItem(), setupStage));

        // Change key bind
        Button changeKeyBindButton = new Button("Set Keybind", getMenuIcon("keybind.png"));
        changeKeyBindButton.setFocusTraversable(false);
        addButtonAction(changeKeyBindButton, ()-> changeKeyBind(setupTableView.getSelectionModel().getSelectedItem()));

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

        addCloseEventHandler(setupStage, false);

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
     * Processes the add button action
     */
    private static void processAddButtonAction() {
        Action newAction = new Action(/*configTable,*/ generateRandomActionName(), 0, false, false, false, ActionTier.BASIC_ABILITY, RESOURCES_PATH + "placeholder.png", ActionStyle.NONE);

        // Lets create the action image so it updates on scren
        newAction.setActionImage(new ImageView(new Image(new File(newAction.getIconPath()).toURI().toString())));

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
            System.out.println("data doesn't exist, nothing is being deleted!");
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
            System.out.println("Stage " + stage.toString() + " is closing");
            stage.close();
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
        setupTableView.refresh();
        setupTableView.getItems().forEach(p-> {
            p.setActionImage(new ImageView(new Image(new File(p.getIconPath()).toURI().toString())));
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

    /**
     * Changes the key bind
     */
    private static void changeKeyBind(Action action) {
        if (action == null) {
            System.out.println("Action isn't selected");
            return;
        }
        Optional<Action> optAction = cachedActions.stream().filter(p-> p.getActionName().toLowerCase().equals(action.getActionName().toLowerCase())).findFirst();
        if (!optAction.isPresent()) {
            System.out.println("action doesn't exist in database");
        }

        System.out.println("changeKeyBind("+action.getActionName()+")");
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
    private static void toggleIdleMode(boolean disable) {
        if (disable) {
            isCombatMode = false;
        } else {
            isCombatMode = !isCombatMode;
        }
        System.out.println("Idle mode is now " + (isCombatMode ? "enabled" : "disabled"));
        lastKeyPressed = new LastKeyPressed(LocalTime.now(), NativeKeyEvent.VC_F11);
        actionStatus = (isCombatMode ? ActionStatus.LOGGING : ActionStatus.PAUSED);
        updateTitle(mainStage);
        if (!isCombatMode) {
            stopResume.setText(stopResumeLabel[2][0]);
            System.out.println("stop logging");
        } else {
            stopResume.setText(stopResumeLabel[1][0]);
            System.out.println("start logging");
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int key = nativeKeyEvent.getKeyCode();

        Platform.runLater(() -> {
            if (!isCombatMode) {
                System.out.println("Player is in idle mode");
                return;
            }
            for (Action action : cachedActions) {
                if (action.getActionStyle() != COMBAT_STYLE
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
