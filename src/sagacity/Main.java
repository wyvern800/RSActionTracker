package sagacity;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import objects.*;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import utils.FileWritter;
import utils.MasksConstants;

import java.awt.*;
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
    /**
     * The combat style you'l be using on the abilities
     * ActionStyle.MELEE For melee | ActionStyle.RANGED for ranged | ActionStyle.MAGIC for magic
     */
    private static final ActionStyle COMBAT_STYLE = ActionStyle.RANGED;
    /**
     * The title
     */
    private static String title = "RSActionLogger - by wyvern800"; //Do not touch

    /**
     * {@code True} if in keys should be logged, {@code False} if not
     */
    private boolean isCombatMode = false;

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

    private static Label start = new Label("Press F12 to toggle combat mode to start logging your actions.");

    /**
     * The constructor
     */
    public Main()  {
        scenes = new ArrayList<>();
        cachedActions = new ArrayList<>(MAX_SIZE);
        actions = new ArrayList<>();

        // Adds all abilities to the cache
        List<Action> tempList = new ArrayList<>();

        for (ActionList actionList: ActionList.values()) {
            if (actionList.getAction().getActionStyle() != COMBAT_STYLE
                    && actionList.getAction().getActionStyle() != ActionStyle.NONE)
                continue;
            tempList.add(actionList.getAction());
        }
        cachedActions.addAll(tempList);

        // Adds the listener
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        GlobalScreen.addNativeKeyListener(this);
    }

    /**
     * Updates the screen with the actions
     */
    private static void update() {
        for (int i = 0; i < actions.size(); i++) {
            Action actionToBeAdded = new Action(actions.get(i).getActionName(), actions.get(i).getPressedKey(), actions.get(i).isCtrlPressed(),actions.get(i).isShiftPressed(), actions.get(i).isAltPressed(), actions.get(i).getActionTier(), actions.get(i).getActionImage().getImage(), actions.get(i).getActionStyle());

            HBox hbox = new HBox(1);
            Group group = new Group();

            Label actionName = new Label(actionToBeAdded.getActionName());
            actionName.setStyle("-fx-text-fill: #fff; -fx-font-size: 15px; -fx-effect: dropshadow( one-pass-box , black , 10 , 0.0 , 2 , 0 )");
            actionName.setAlignment(Pos.CENTER);
            actionName.setPrefSize(100, 100);

            hbox.setSpacing(2); // Space between the squares
            hbox.setPrefSize(60, 60);
            hbox.setStyle("-fx-border-style: solid solid solid solid;\n" +
                    "-fx-border-width: 2;\n" +
                    "-fx-border-color: "+actionToBeAdded.getActionTier().getAbilityBorder());

            group.getChildren().add(actionToBeAdded.getActionImage());

            if (showAbilityName) {
                group.getChildren().add(actionName);
            }

            hbox.getChildren().add(group);

            gridPane.add(hbox, i, 0);
        }
    }

    /**
     * Starts the program
     * @param mainStage The main stage
     */
    @Override
    public void start(Stage mainStage) {
        // create the menus
        final Menu menu = new Menu("Menu");
        final Menu abilities = new Menu("Abilities");
        addMenuAction(abilities, () -> System.out.println("WIP"));
        final Menu help = new Menu("Help");
        addMenuAction(help, () -> {
            try {
                Desktop.getDesktop().browse(new URL("https://github.com/wyvern800/RSActionLogger/blob/master/README.md").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
        // create menu items
        final MenuItem menuItem = new MenuItem("Toggle combat mode");
        addMenuItemAction(menuItem, () -> {
            isCombatMode = !isCombatMode;
            System.out.println("Combat mode is now "+(isCombatMode? "enabled": "disabled"));
        });
        // add menu items to menu
        menu.getItems().add(menuItem);
        // create a menubar
        MenuBar menuBar = new MenuBar();
        // add menu to menubar
        menuBar.getMenus().addAll(menu, abilities, help);
        // create a VBox
        VBox verticalBox = new VBox(menuBar);
        // create a scene
        Scene mainScene = new Scene(verticalBox, 1100, 137);
        // set a background color to the vertical box
        verticalBox.setStyle("-fx-background-color: #000000;");
        // create a grid pane
        gridPane = new GridPane();
        // add the grid pane to the vertical box
        verticalBox.getChildren().add(gridPane);

        mainStage.setTitle("RSActionLogger - by wyvern800 - Press F12 to enable/disable combat mode");
        mainStage.setScene(mainScene);
        mainStage.setAlwaysOnTop(true);
        mainStage.setResizable(false);
        mainStage.centerOnScreen();
        mainStage.sizeToScene();
        mainStage.show();

        // Center the window to screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        mainStage.setX((screenBounds.getWidth() - mainStage.getWidth()) / 2);
        mainStage.setY((screenBounds.getHeight() - mainStage.getHeight()) / 2);

        Main.mainStage = mainStage;
    }

    /**
     * The bootstrapper
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
     * @param menuItem The menuItem we are clicking
     * @param action The action that should it do
     */
    private static void addMenuItemAction(MenuItem menuItem, Runnable action) {
        menuItem.setOnAction(event -> {
            action.run();
            System.out.println("clicked at "+menuItem.getText());
        });
    }

    /**
     * Adds a menu action when clicking on them
     * @param menu The menuItem we are clicking
     * @param action The action that should it do
     */
    private static void addMenuAction(Menu menu, Runnable action) {
        final MenuItem menuItem = new MenuItem();
        menu.getItems().add(menuItem);

        menu.addEventHandler(Menu.ON_SHOWN, event -> menu.hide());
        menu.addEventHandler(Menu.ON_SHOWING, event -> {
            menu.fire();
            action.run();
            System.out.println("clicked at "+menu.getText());
            event.consume();
        });
    }

    /**
     * Represents the control down
     * @param e The key event
     * @return {@code True} if yes {@code False} if not
     */
    private boolean isControlDown(final NativeKeyEvent e ) {
        return (e.getModifiers() & CTRL_MASK) != 0;
    }

    /**
     * Represents the shift down
     * @param e The key event
     * @return {@code True} if yes {@code False} if not
     */
    private boolean isShiftDown(final NativeKeyEvent e ) {
        return (e.getModifiers() & SHIFT_MASK) != 0;
    }

    /**
     * Represents the alt down
     * @param e The key event
     * @return {@code True} if yes {@code False} if not
     */
    private boolean isAltDown(final NativeKeyEvent e ) {
        return (e.getModifiers() & ALT_MASK) != 0;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int key = nativeKeyEvent.getKeyCode();

        // Toggle combat mode
        if (key == NativeKeyEvent.VC_F12) {
            isCombatMode = !isCombatMode;
            System.out.println("Combat mode is now "+(isCombatMode? "enabled": "disabled"));
            lastKeyPressed = new LastKeyPressed(LocalTime.now(), nativeKeyEvent.getKeyCode());
        }

        Platform.runLater( () -> {
            if (!isCombatMode) {
                System.out.println("Player is in idle mode");
                return;
            }
            for (Action action : cachedActions) {
                // If the pressed key exists in our actions array
                if ((isControlDown(nativeKeyEvent)) && (action.getPressedKey() == key)) { // Ctrl pressed
                    if (!action.isCtrlPressed())
                        continue;
                    processKeyAction(action, nativeKeyEvent);
                    System.out.println("control+" + key);
                } else if ((isShiftDown(nativeKeyEvent)) && (action.getPressedKey() == key)) { // Shift pressed
                    if (!action.isShiftPressed())
                        continue;
                    processKeyAction(action, nativeKeyEvent);
                    System.out.println("shift+" + key);
                } else if ((isAltDown(nativeKeyEvent)) && (action.getPressedKey() == key)) { // Alt pressed
                    if ( !action.isAltPressed())
                        continue;
                processKeyAction(action, nativeKeyEvent);
                System.out.println("alt+" + key);
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
        } );
    }

    /**
     * Processes the key action
     * @param action The key action
     */
    private static void processKeyAction(Action action, NativeKeyEvent nativeKeyEvent) {
        int key = nativeKeyEvent.getKeyCode();

        if ((key == lastKeyPressed.getKeyCode())) {
            System.out.println("Key already pressed...");
            return;
        }
        // Handles the keys pressing
        actions.add(action);

        // updates the screen
        update();
        if (actions.size() > MAX_SIZE-1) {
            actions.remove(0); // Removes the first element if list is full
            gridPane.getChildren().remove(0);
        }

        actionsDone++;
        mainStage.setTitle(title + " - actions=" + actionsDone);
        FileWritter.write(TOTAl_ACTIONS[0], TOTAl_ACTIONS[1], new String[] {Integer.toString(actionsDone)});
        lastKeyPressed = new LastKeyPressed(LocalTime.now(), nativeKeyEvent.getKeyCode());

        // Prints the key we pressed
        if (isDebugMode)
        System.out.println("pressedKey='" + action.getPressedKey() + "', ability='" + action.getActionName() + "', borderColor='" + action.getActionTier().getAbilityBorder().toString() + "'");
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    }
}
