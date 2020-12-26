package objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jnativehook.keyboard.NativeKeyEvent;
import sagacity.Main;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents an action
 *
 * @author wyvern800 - http://github.com/wyvern800
 * @created 04/12/2020 - 03:36
 * @project RSActionTracker
 */
public class Action {
    //private TableView tableView;
    private String actionName;
    private int pressedKey;
    //private transient String pressedKeyName;
    private boolean ctrlPressed;
    private transient BooleanProperty ctrlBooleanProperty;
    private boolean shiftPressed;
    private transient BooleanProperty shiftBooleanProperty;
    private boolean altPressed;
    private transient BooleanProperty altBooleanProperty;
    private ActionTier actionTier;
    private String iconPath;
    private transient ImageView actionImage;
    private ActionStyle actionStyle;

    /*public TableView getTableView() {
        return tableView;
    }*/

    /*public void setTableView(TableView tableView) {
        this.tableView = tableView;
    }*/

    public BooleanProperty shiftBooleanPropertyProperty() {
        return shiftBooleanProperty;
    }

    public void setShiftBooleanProperty(boolean shiftBooleanProperty) {
        this.shiftBooleanProperty.set(shiftBooleanProperty);
        this.shiftPressed = shiftBooleanProperty;
        //System.out.println("setShiftBooleanProperty: "+shiftBooleanProperty);
    }

    public boolean isShiftCheckboxChecked() {
        if (shiftBooleanProperty == null) {
            shiftBooleanProperty = new SimpleBooleanProperty(isShiftPressed());
        }
        return shiftBooleanProperty.get();
    }


    public BooleanProperty ctrlBooleanPropertyProperty() {
        return ctrlBooleanProperty;
    }

    public void setCtrlBooleanProperty(boolean ctrlBooleanProperty) {
        this.ctrlBooleanProperty.set(ctrlBooleanProperty);
        this.ctrlPressed = ctrlBooleanProperty;
        //System.out.println("setCtrlBooleanProperty: "+ctrlBooleanProperty);
    }

    public boolean isCtrlCheckboxChecked() {
        if (ctrlBooleanProperty == null) {
            ctrlBooleanProperty = new SimpleBooleanProperty(isCtrlPressed());
        }
        return ctrlBooleanProperty.get();
    }

    public BooleanProperty altBooleanPropertyProperty() {
        return altBooleanProperty;
    }

    public void setAltBooleanProperty(boolean altBooleanProperty) {
        this.altBooleanProperty.set(altBooleanProperty);
        this.altPressed = altBooleanProperty;
        //System.out.println("setShiftBooleanProperty: "+altBooleanProperty);
    }

    public boolean isAltCheckboxChecked() {
        if (altBooleanProperty == null) {
            altBooleanProperty = new SimpleBooleanProperty(isAltPressed());
        }
        return altBooleanProperty.get();
    }

    public String getPressedKeyName() {
        return NativeKeyEvent.getKeyText(pressedKey);
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setPressedKey(int pressedKey) {
        this.pressedKey = pressedKey;
    }

    public void setCtrlPressed(boolean ctrlPressed) {
        this.ctrlPressed = ctrlPressed;
    }

    public void setShiftPressed(boolean shiftPressed) {
        this.shiftPressed = shiftPressed;
    }

    public void setAltPressed(boolean altPressed) {
        this.altPressed = altPressed;
    }

    public void setActionTier(ActionTier actionTier) {
        this.actionTier = actionTier;
    }

    public void setActionImage(ImageView actionImage) {
        this.actionImage = actionImage;
    }

    public void setActionStyle(ActionStyle actionStyle) {
        this.actionStyle = actionStyle;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    /**
     * Gets the action tier
     * @return The action tier
     */
    public ActionTier getActionTier() {
        return actionTier;
    }

    /**
     * Checks if shift is pressed
     * @return {@code True} if yes, {@code False} if not
     */
    public boolean isShiftPressed() {
        return shiftPressed;
    }

    /**
     * Checks if alt is pressed
     * @return {@code True} if yes, {@code False} if not
     */
    public boolean isAltPressed() {
        return altPressed;
    }

    /**
     * Checks if ctrl is pressed
     * @return {@code True} if yes, {@code False} if not
     */
    public boolean isCtrlPressed() {
        return ctrlPressed;
    }

    /**
     * Gets the pressedKeyCode
     * @return the pressedKeyCode
     */
    public int getPressedKey() {
        return pressedKey;
    }

    /**
     * Gets the actionImage
     * @return The actionImage
     */
    public ImageView getActionImage() {
        return actionImage;
    }

    /**
     * Gets the actionName
     * @return The actionName
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Gets tje actionStyle
     * @return The actionStyle
     */
    public ActionStyle getActionStyle() {
        return actionStyle;
    }


    /**
     * Represents an action
     * @param pressedKey The pressedKey
     * @param iconPath The actionImage
     */
    public Action(/*TableView table, */String actionName, int pressedKey, boolean ctrlPressed, boolean shiftPressed, boolean altPressed, ActionTier tier, String iconPath, ActionStyle style) {
        //this.tableView = table;
        this.actionName = actionName;
        this.pressedKey = pressedKey;
        this.ctrlPressed = ctrlPressed;
        this.ctrlBooleanProperty = new SimpleBooleanProperty(ctrlPressed);
        this.shiftPressed = shiftPressed;
        this.shiftBooleanProperty = new SimpleBooleanProperty(shiftPressed);
        this.altPressed = altPressed;
        this.altBooleanProperty = new SimpleBooleanProperty(altPressed);
        this.actionImage = new ImageView(new Image(new File(iconPath).toURI().toString()));
        this.actionTier = tier;
        this.iconPath = iconPath;
        this.actionStyle = style;
        //actionImage.setFitWidth(104);
        actionImage.setFitWidth(30);
        actionImage.setFitHeight(30);
        actionImage.setPreserveRatio(true);
        actionImage.setSmooth(true);
        actionImage.setCache(true);
        actionImage.setClip(null); // remove the rounding clip so that our effect can show through.
        actionImage.setPickOnBounds(true);
    }

}
