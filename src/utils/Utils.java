package utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import objects.ActionStatus;
import objects.LastKeyPressed;
import org.jnativehook.keyboard.NativeKeyEvent;
import sagacity.Constants;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Utiliies holder
 *
 * @author wyvern800 - http://github.com/wyvern800
 * @created 26/12/2020 - 05:35
 * @project RSActionTracker
 */
public class Utils implements MasksConstants, Constants {
    /**
     * Returns if the key is a prohibited to be bound
     * @param nke The nativeKeyEvent
     * @return {@code True} if the key is free to use | {@code False} if the key is prohibited to use
     */
    public static boolean isProhibitedKey(NativeKeyEvent nke) {
        switch (nke.getKeyCode()) {
            case NativeKeyEvent.VC_ESCAPE:
            case NativeKeyEvent.VC_F12:
            case NativeKeyEvent.VC_DELETE:
            case NativeKeyEvent.VC_PRINTSCREEN:
            case NativeKeyEvent.VC_PAUSE:
            case NativeKeyEvent.VC_SCROLL_LOCK:
            case NativeKeyEvent.VC_ENTER:
            case NativeKeyEvent.VC_BACKSPACE:
                return true;
            default:
                return false;
        }
    }

    /**
     * Represents the control down
     *
     * @param e The key event
     * @return {@code True} if yes {@code False} if not
     */
    public static boolean isControlDown(final NativeKeyEvent e) {
        return (e.getModifiers() & CTRL_MASK) != 0;
    }

    /**
     * Represents the shift down
     *
     * @param e The key event
     * @return {@code True} if yes {@code False} if not
     */
    public static boolean isShiftDown(final NativeKeyEvent e) {
        return (e.getModifiers() & SHIFT_MASK) != 0;
    }

    /**
     * Represents the alt down
     *
     * @param e The key event
     * @return {@code True} if yes {@code False} if not
     */
    public static boolean isAltDown(final NativeKeyEvent e) {
        return (e.getModifiers() & ALT_MASK) != 0;
    }

    /**
     * Create a table column
     *
     * @param column       - the table column
     * @param preferredSize The preferred size {@code null} for automatic
     * @param prop         - The properties
     * @return The generated TableColumn
     */
    public static TableColumn addTableColumn(TableColumn column, Integer preferredSize, PropertyValueFactory prop, boolean isEditable) {
        if (preferredSize != null) {
            column.setMinWidth(preferredSize);
        }
        column.setCellValueFactory(prop);
        column.setEditable(isEditable);
        return column;
    }

    /**
     * Shows a confirmation dialog pane
     * @param contentText The description about the panel
     * @param headerText The headerText
     * @param owner Which stage was this called from (the owner)
     * @param exec The execution when clicking on OK button
     */
    public static void showLinkToPatreonDialog(String title, String headerText, String contentText, Stage owner, Runnable exec) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.initOwner(owner);
        a.setTitle(title);
        a.setHeaderText(headerText);
        a.setContentText(contentText);
        a.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(response -> exec.run());
    }

    /**
     * Shows an information dialog
     * @param title The dialog title
     * @param headerText The header text
     * @param contentText The contentText
     * @param owner The owner who called
     */
    public static void showInformationDialog(String title, String headerText, String contentText, Stage owner) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.initOwner(owner);
        a.setTitle(title);
        a.setHeaderText(headerText);
        a.setContentText(contentText);

        a.showAndWait();
    }

    /**
     * Show errro dialog
     * @param contentText The description about the panel
     * @param owner Which stage was this called from (the owner)
     * @param exec The execution when clicking on OK button
     */
    public static void showErrorDialog(String contentText, Stage owner, Runnable exec) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.initOwner(owner);
        if (contentText != null) {
            a.setContentText(contentText);
        }
        a.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    if (exec != null) {
                        exec.run();
                    }
                });
    }


    /**
     * Shows a confirmation dialog pane
     * @param contentText The description about the panel
     * @param owner Which stage was this called from (the owner)
     * @param exec The execution when clicking on OK button
     */
    public static void showConfirmationDialog(String contentText, Stage owner, Runnable exec) {
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
     * Adds an icon to the stage
     */
    public static void addStageIcon(Stage stage) {
        Image anotherIcon = new Image(ICONS_PATH + "/" + "log.png");
        stage.getIcons().add(anotherIcon);
    }

    /**
     * Centers the window
     *
     * @param stage The stage
     */
    public static void centerScreen(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }


    /**
     * Updates the title
     * @param stage The stage
     * @param title The title
     * @param actionStatus The actionStatus
     * @param actionsDone The actionsDone
     */
    public static void updateTitle(Stage stage, String title, ActionStatus actionStatus, int actionsDone) {
        stage.setTitle(title + " | status=" + actionStatus + " | actionsDone=" + actionsDone);
    }


    /**
     * Returns a random name for the action adding name
     *
     * @return The ability name
     */
    public static String generateRandomActionName() {
        String[] prefixes = {"Pretty", "Ugly", "Incredible", "Amazing", "Delicious", "Matty's", "Crazy", "Special", "Great", "Splendorous", "Dirty", "Poopy"};
        Random random = new Random();
        int randomInteger = random.nextInt(prefixes.length);
        return prefixes[randomInteger] + " Action";
    }

    /**
     * Adds a menuItem action when clicking on them
     *
     * @param menuItem The menuItem we are clicking
     * @param action   The action that should it do
     */
    public static void addMenuItemAction(MenuItem menuItem, Runnable action) {
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
    public static void addMenuAction(Menu menu, Runnable action) {
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
     * Calcs the actions per sec
     * @param events The event collection
     * @return the value
     */
    private static double calcActionsPerSec(Collection<Long> events) {
        long max = events.stream().reduce(Long::max).orElse(0L);
        long min = events.stream().reduce(Long::min).orElse(0L);
        if (max == min) {
            return 0;
            //throw new IllegalArgumentException("We need at least two events in different times to be able to calc.");
        }
        System.out.println("min: "+(int) min + " max:" +(int) max);
        return events.size() / (double) (max - min);
    }

    /**
     * Actions per se c
     * @param events The collection
     * @return The value
     */
    public static double actionsPerSec(Collection<LastKeyPressed> events) {
        return calcActionsPerSec(events.stream().map(LastKeyPressed::getUsedTimeLong).collect(Collectors.toList()));
    }
}
