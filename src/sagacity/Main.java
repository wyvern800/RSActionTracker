package sagacity;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.*;
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
import utils.FileWritter;
import utils.GsonUtil;
import utils.MasksConstants;
import javafx.scene.image.Image;

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

/**
 * Software used to log the actions from the player (this is not a keylogger)
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 04/12/2020 - 14:14
 * @project RSActionsLogging
 */
public class Main extends Application implements NativeKeyListener, Constants, MasksConstants {
    private static SavedData savedData;

    public static SavedData getSavedData() {
        return savedData;
    }

    /**
     * The combat style you'l be using on the abilities
     * ActionStyle.MELEE For melee | ActionStyle.RANGED for ranged | ActionStyle.MAGIC for magic
     */
    private static final ActionStyle COMBAT_STYLE = ActionStyle.RANGED;
    /**
     * The title
     */
    private static String title = "RSActionLogger - by wyvern800"; //Do not touch

    // The actionStatus
    private static ActionStatus actionStatus = ActionStatus.IDLE;

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

    /**
     * The grid pane
     */
    public static GridPane gridPane;

    private static TableView<Action> configTable;

    /**
     * The main scene
     */
    public static Scene mainScene;

    /**
     * The scenes
     */
    private static List<Scene> scenes;

    /**
     * Cached actions
     */
    public static List<Action> cachedActions;

    /**
     * The personal pressed actions
     */
    public static List<Action> actions;

    /**
     * Max actions we're monitoring
     */
    private static final int MAX_SIZE = 10; //Do not touch

    /**
     * The constructor
     */
    public Main() {
        // Initializations
        scenes = new ArrayList<>();
        cachedActions = new ArrayList<>(MAX_SIZE);
        actions = new ArrayList<>();
        //cachedActions = getSavedData().getCachedActions();
        //actions = getSavedData().getActions();

        // Load all actions to our cache
        List<Action> tempActList = new ArrayList<>();
        for (ActionList store : ActionList.values()) {
            tempActList.add(store.getAction());
            cachedActions.add(store.getAction());
        }

        // Counter
        List<String> tempActNames = new ArrayList<>();
        for (Action store : tempActList) {
            tempActNames.add(store.getActionName());
        }
        System.out.println("totalLoaded=" + tempActNames.size() + ", actionsList=" + Arrays.toString(tempActNames.toArray()));

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
    private static void addActionToScreen(int i) {
        // Creating the action instance
        Action newAction = new Action(/*configTable,*/ actions.get(i).getActionName(), actions.get(i).getPressedKey(), actions.get(i).isCtrlPressed(), actions.get(i).isShiftPressed(), actions.get(i).isAltPressed(), actions.get(i).getActionTier(), actions.get(i).getActionImage().getImage(), actions.get(i).getActionStyle());

        // Creating the content boxes
        HBox actionBox = new HBox(1);
        Group actionGroup = new Group();

        // Sets the backgrounds of the actionBoxes to black
        actionGroup.setStyle("-fx-background-color: #000000;");
        actionBox.setStyle("-fx-background-color: #000000;");

        // Creating the actionName label
        Label actionName = new Label(newAction.getActionName());
        actionName.setStyle("-fx-text-fill: #fff; -fx-font-size: 15px; -fx-effect: dropshadow( one-pass-box , black , 10 , 0.0 , 2 , 0 )");
        actionName.setAlignment(Pos.CENTER);
        actionName.setPrefSize(100, 100);
        actionBox.setSpacing(2); // Space between the squares
        actionBox.setPrefSize(60, 60);
        actionBox.setStyle("-fx-border-style: solid solid solid solid;\n" +
                "-fx-border-width: 2;\n" +
                "-fx-border-color: " + newAction.getActionTier().getAbilityBorder());

        actionGroup.getChildren().add(newAction.getActionImage());
        actionGroup.getChildren().add(actionName);

        actionBox.getChildren().add(actionGroup);

        actionName.setVisible(showActionName);

        // Add the actionBox to the gridPane
        gridPane.add(actionBox, i, 0);
    }

    /**
     * Updates the screen with the actions
     */
    private static void updateScreen() {
        // Loops through the personal actions and set them to screen
        for (int i = 0; i < actions.size(); i++) {
            addActionToScreen(i);
        }
    }

    private static Menu stopResume = new Menu("Stop");
    private static final String[][] stopResumeLabel = {{"Start Logging", "list.png"}, {"Stop Logging", "list.png"}, {"Resume Logging", "list.png"}};

    /**
     * Starts the program
     *
     * @param mainStage The main stage
     */
    @Override
    public void start(Stage mainStage) {
        configTable = new TableView<>();

        // create the menus
        final Menu menu = new Menu("Toggles", getMenuIcon("list.png"));
        final Menu configurations = new Menu("Setup", getMenuIcon("setup.png"));
        //addMenuAction(abilities, () -> System.out.println("WIP"));
        final Menu help = new Menu("Help", getMenuIcon("help.png"));
        stopResume = new Menu(stopResumeLabel[0][0], getMenuIcon(stopResumeLabel[0][1]));
        addMenuAction(help, () -> {
            try {
                Desktop.getDesktop().browse(new URL("https://github.com/wyvern800/RSActionLogger/blob/master/README.md").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
        // Combat menu
        final MenuItem toggleCombatMode = new MenuItem("Toggle Combat mode", getMenuIcon("config.png"));
        final MenuItem toggleAbilityName = new MenuItem("Toggle Ability name", getMenuIcon("config.png"));
        // add menu items to menu
        menu.getItems().addAll(toggleCombatMode, toggleAbilityName);

        addMenuItemAction(toggleCombatMode, ()-> toggleCombatMode(false));

        addMenuItemAction(toggleAbilityName, () -> {
            showActionName = !showActionName;
            System.out.println("Ability name is now " + (showActionName ? "enabled" : "disabled"));
        });

        addMenuAction(stopResume, ()-> toggleCombatMode(false));

        // Configurations menu
        final MenuItem configureAbilities = new MenuItem("Actions", getMenuIcon("config.png"));
        final MenuItem actionStyle = new MenuItem("Action Styles", getMenuIcon("config.png"));
        actionStyle.setDisable(true);
        final MenuItem actionTier = new MenuItem("Action Tiers", getMenuIcon("config.png"));
        actionTier.setDisable(true);
        addMenuItemAction(configureAbilities, ()-> {
            toggleCombatMode(true);
            openSetupScreen();
        });
        addMenuItemAction(actionStyle, Main::openActionStyles);
        addMenuItemAction(actionTier, Main::openActionTiers);
        configurations.getItems().addAll(configureAbilities, actionStyle, actionTier);

        // add menu to menubar
        // Left bar
        MenuBar leftBar = new MenuBar();
        leftBar.getMenus().addAll(menu, configurations, help);
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
        gridPane = new GridPane();
        // add the grid pane to the vertical box
        verticalBox.getChildren().add(gridPane);

        mainStage.setTitle(title);
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
                    toggleCombatMode(false);
                }
        );


        addCloseEventHandler(mainStage, true);

        loadDatabase();
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

    /*private static void test() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("config.fxml"));
        Stage stage = new Stage();

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("TableView App");
        stage.show();
    }*/

    private static void openActionTiers() {
        System.out.println("actionTiers");
    }

    private static void openActionStyles() {
        System.out.println("actionStyles");
    }

    /**
     * Opens the setup screen
     */
    private static void openSetupScreen() {
        ObservableList<Action> observableListData = FXCollections.observableArrayList(cachedActions);

        // Resizes the icons to show for the setup screen
        observableListData.forEach(p -> {
            p.getActionImage().setFitWidth(30);
            p.getActionImage().setFitHeight(30);
        });

        Stage setupStage = new Stage();

        Scene scene = new Scene(new Group());
        setupStage.setTitle("Action Management");
        setupStage.setWidth(600);
        setupStage.setHeight(550);

        final Label label = new Label("ActionsList");

        configTable.setEditable(true);

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
        TableColumn keyCode = addTableColumn(new TableColumn<Action, Integer>("KeyCode"), null, new PropertyValueFactory<Action, Integer>("pressedKey"), false);
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
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setShiftPressed(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setShiftPressed(t.getNewValue());

                    getSavedData().setCachedActions(cachedActions);
                }
        );
        shiftMask.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, Boolean>>) t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setShiftPressed(t.getNewValue());
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
        actionImage.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Action, ImageView>>) t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setActionImage(t.getNewValue());
                    cachedActions.get(t.getTablePosition().getRow()).setActionImage(t.getNewValue());

            getSavedData().setCachedActions(cachedActions);
                }
        );
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

        configTable.setItems(observableListData);
        configTable.getColumns().addAll(actionName, actionImage, keyCode, ctrlMask, shiftMask, altMask, actionTier, actionStyle);

        HBox pane = new HBox();
        pane.setSpacing(10);

        // Action adding button
        Button addButton = new Button("Add Action", Main.getMenuIcon("plus.png"));
        /*scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.INSERT),
                addButton::fire
        );*/
        Main.addButtonAction(addButton, Main::processAddButtonAction);

        // Action removing button
        Button removeButton = new Button("Remove Action", getMenuIcon("remove.png"));
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.DELETE),
                removeButton::fire
        );
        addButtonAction(removeButton, () ->
                showConfirmationDialog(
                        "Are you sure you want to delete: " + configTable.getSelectionModel().getSelectedItem().getActionName() + "?",
                        Main::processRemoveButtonAction));

        // Refresh list button
        Button refreshButton = new Button("Refresh List", getMenuIcon("refresh.png"));
        addButtonAction(refreshButton, Main::refreshTable);

        // Change action icon button
        Button changeIconButton = new Button("Change Ability Icon", getMenuIcon("refresh.png"));
        addButtonAction(changeIconButton, Main::changeAbilityIcon);

        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);

        Separator separator3 = new Separator();
        separator3.setOrientation(Orientation.VERTICAL);

        HBox hBox = new HBox();

        TitledPane tp = new TitledPane();
        tp.setContent(pane);
        tp.setText("Options");
        tp.setExpanded(true);

        hBox.getChildren().add(tp);

        pane.getChildren().addAll(addButton, removeButton, separator2, changeIconButton, separator3, refreshButton);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        //Vertical separator
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        vbox.getChildren().addAll(label, configTable, separator, hBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        setupStage.setAlwaysOnTop(true);
        setupStage.setResizable(false);
        setupStage.setScene(scene);
        setupStage.show();
        configTable.setStyle("text-align: right");

        configTable.setMaxWidth(576);

        tp.setPrefWidth(576);

        centerScreen(setupStage);

        addCloseEventHandler(setupStage, false);

        refreshTable();
    }

    /**
     * Shows a confirmation dialog
     *
     * @param contentText The contentText
     */
    private static void showConfirmationDialog(String contentText, Runnable exec) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.initOwner(mainStage);
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
        System.out.println("added a row");
        Action newAction = new Action(/*configTable,*/ generateRandomActionName(), 0, false, false, false, ActionTier.BASIC_ABILITY, new Image(PLACEHOLDER_PATH + "/placeholder.png"), ActionStyle.NONE);
        newAction.getActionImage().setFitWidth(30);
        newAction.getActionImage().setFitHeight(30);
        configTable.getItems().add(newAction);
        cachedActions.add(newAction);

        getSavedData().setCachedActions(cachedActions);

        /*configTable.getFocusModel().focus(25, null);*/
        configTable.requestFocus();

        configTable.scrollTo(newAction);

        refreshTable();
    }

    /**
     * Processes the remove button action
     */
    private static void processRemoveButtonAction() {
        if (configTable.getSelectionModel() == null || configTable.getSelectionModel().isEmpty()) {
            System.out.println("Please select a row before deleting!");
            return;
        }
        Action selectedRow = configTable.getSelectionModel().getSelectedItem();

        // Just a normal check to avoid things from being deleted while not present on our list
        /*Optional<Action> optAction = cachedActions.stream().filter(a-> a.getActionName().toLowerCase().equals(selectedRow.getActionName().toLowerCase())).findFirst();
        if (!optAction.isPresent()) {
            System.out.println("data doesn't exist, nothing is being deleted!");
            return;
        }*/

        System.out.println("removed='" + selectedRow.getActionName() + "'");
        configTable.getItems().remove(configTable.getSelectionModel().getSelectedItem());

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
        configTable.refresh();
        System.out.println("Table refreshed");
    }

    /**
     * Change the ability icon
     */
    private static void changeAbilityIcon() {
        System.out.println("changeAbilityIcon");
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
     * Toggles the combat mode
     */
    private static void toggleCombatMode(boolean disable) {
        if (disable) {
            isCombatMode = false;
        } else {
            isCombatMode = !isCombatMode;
        }
        System.out.println("Combat mode is now " + (isCombatMode ? "enabled" : "disabled"));
        lastKeyPressed = new LastKeyPressed(LocalTime.now(), NativeKeyEvent.VC_F11);
        actionStatus = (isCombatMode ? ActionStatus.LOGGING : ActionStatus.PAUSED);
        updateTitle(mainStage);
        if (!isCombatMode) {
            stopResume.setText(stopResumeLabel[2][0]);
            System.out.println("start logging");
        } else {
            stopResume.setText(stopResumeLabel[1][0]);
            System.out.println("stop logging");
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
            actions.forEach(action -> {
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

        // Handles the keys pressing
        actions.add(action);

        if (actions.size() > MAX_SIZE) {
            actions.remove(0); // Removes the first element if list is full
            gridPane.getChildren().remove(0);
        }

        actionsDone++;

        FileWritter.write(TOTAl_ACTIONS[0], TOTAl_ACTIONS[1], new String[]{Integer.toString(actionsDone)});
        lastKeyPressed = new LastKeyPressed(LocalTime.now(), nativeKeyEvent.getKeyCode());
        updateTitle(mainStage);

        // updates the screen
        updateScreen();

        // Prints the key we pressed
        System.out.println("actionName='" + action.getActionName() + "', key=" + (action.isCtrlPressed() ? "CTRL+" : action.isShiftPressed() ? "Shift+" : action.isAltPressed() ? "ALT+" : "") + key + ");");
    }

    private static void updateTitle(Stage stage) {
        stage.setTitle(title + " | status=" + actionStatus + " | actionsDone=" + actionsDone + " | user=Sagacity");
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
