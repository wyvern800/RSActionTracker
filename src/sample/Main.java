package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import objects.Action;
import objects.ActionList;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import utils.FileWritter;
import java.util.*;
import java.util.logging.Level;

public class Main extends Application implements NativeKeyListener, Constants {
    private static String title = "RSActionLogger - by mattFerreira";

    /**
     * {@code True} if in keys should be logged, {@code False} if not
     */
    private boolean isCombatMode = false;

    /**
     * Variables
     */
    private static Stage mainStage;
    private static int actionsDone;

    /**
     * The grid pane
     */
    public static GridPane gridPane;

    /**
     * The main scene
     */
    public static Scene MAIN_SCENE;

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
    private static List<Action> actions;

    /**
     * Max actions we're monitoring
     */
    private static final int MAX_SIZE = 10;

    private static Label start = new Label("Press F12 to toggle combat mode to start logging your actions.");

    /**
     * The constructor
     */
    public Main()  {
        scenes = new ArrayList<>();
        cachedActions = new ArrayList<>(MAX_SIZE);
        actions = new ArrayList<>();
        gridPane = new GridPane();
        MAIN_SCENE = new Scene(gridPane, 1022, 97);
        gridPane.setStyle("-fx-background-color: black;");
        gridPane.setPadding(new Insets(3, 3, 3, 3));
        gridPane.setHgap(3);
        gridPane.setVgap(3);

        // Adds all abilities to the cache
        List<Action> tempList = new ArrayList<>();

        for (ActionList actionList: ActionList.values()) {
            tempList.add(actionList.getAction());
        }
        cachedActions.addAll(tempList);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        GlobalScreen.addNativeKeyListener(this);

        // Any
        //cachedActions.addAll(Arrays.asList(tuskaWrath, anguish));
    }

    /**
     * Updates the screen with the actions
     */
    private static void update() {
        for (int i = 0; i < actions.size(); i++) {
            Action actionToBeAdded = new Action(actions.get(i).getActionName(), actions.get(i).getPressedKey(), actions.get(i).isCtrlPressed(),actions.get(i).isShiftPressed(), actions.get(i).isAltPressed(), actions.get(i).getActionTier(), actions.get(i).getActionImage().getImage());
            /*if (!gridPane.getChildren().isEmpty() && gridPane.getChildren().get(i) != null) {
                gridPane.getChildren().remove(i);
            }*/
            /*long end = delays[i] + 3000; // 5 seconds
            while (delays[i] < end)
            {
                delays[i] = System.currentTimeMillis();
                System.out.println("delay");;

            } else {*/ //TODO delay

            gridPane.add(actionToBeAdded.getActionImage(), i, 0);
            Label actionName = new Label(actionToBeAdded.getActionName());
            actionName.setStyle("-fx-text-fill: #fff; -fx-font-size: 15px; -fx-effect: dropshadow( one-pass-box , black , 8 , 0.0 , 2 , 0 )");
            actionName.setMaxWidth(Double.MAX_VALUE);
            actionName.setAlignment(Pos.CENTER);
            gridPane.add(actionName, i, 0);
        }
    }

    /**
     * Starts the program
     * @param mainStage The main stage
     */
    @Override
    public void start(Stage mainStage) {
        mainStage.setTitle("RSActionLogger - by mattFerreira");
        mainStage.setScene(MAIN_SCENE);
        mainStage.setAlwaysOnTop(true);
        mainStage.setResizable(false);
        mainStage.show();
        Main.mainStage = mainStage;
    }

    /**
     * The bootstrapper
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
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
                    processKeyAction(action);
                    System.out.println("control+" + key);
                } else if ((isShiftDown(nativeKeyEvent)) && (action.getPressedKey() == key)) { // Shift pressed
                    if (!action.isShiftPressed())
                        continue;
                    processKeyAction(action);
                    System.out.println("shift+" + key);
                } else if ((isAltDown(nativeKeyEvent)) && (action.getPressedKey() == key)) { // Alt pressed
                    if ( !action.isAltPressed())
                        continue;
                processKeyAction(action);
                System.out.println("alt+" + key);
                } else if (action.getPressedKey() == key && (!action.isCtrlPressed() && !action.isAltPressed() && !action.isShiftPressed())) {
                    processKeyAction(action);
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
    private static void processKeyAction(Action action) {
        // Handles the keys pressing
        actions.add(action);

        // updates the screen
        update();
        if (actions.size() > 9)
            actions.remove(0); // Removes the first element if list is full

        actionsDone++;
        mainStage.setTitle(title + " - actions=" + actionsDone);
        FileWritter.write(actionsDone);

        // Prints the key we pressed
        System.out.println("pressedKey='" + action.getPressedKey() + "', ability='" + action.getActionName() + "', borderColor='" + action.getActionTier().getAbilityBorder().toString() + "'");
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    }
}
