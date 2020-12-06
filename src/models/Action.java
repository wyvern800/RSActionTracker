package models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Default header
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 06/12/2020 - 03:52
 * @project RSKeyLogging
 */
public class Action {
    private final SimpleStringProperty actionName;
    private final SimpleIntegerProperty pressedKey;
    private final SimpleBooleanProperty ctrlPressed;
    private final SimpleBooleanProperty shiftPressed;
    private final SimpleBooleanProperty altPressed;
    private final SimpleObjectProperty actionTier;
    private final SimpleObjectProperty actionImage;
    private final SimpleObjectProperty actionStyle;

    public String getActionName() {
        return actionName.get();
    }

    public SimpleStringProperty actionNameProperty() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName.set(actionName);
    }

    public int getPressedKey() {
        return pressedKey.get();
    }

    public SimpleIntegerProperty pressedKeyProperty() {
        return pressedKey;
    }

    public void setPressedKey(int pressedKey) {
        this.pressedKey.set(pressedKey);
    }

    public boolean isCtrlPressed() {
        return ctrlPressed.get();
    }

    public SimpleBooleanProperty ctrlPressedProperty() {
        return ctrlPressed;
    }

    public void setCtrlPressed(boolean ctrlPressed) {
        this.ctrlPressed.set(ctrlPressed);
    }

    public boolean isShiftPressed() {
        return shiftPressed.get();
    }

    public SimpleBooleanProperty shiftPressedProperty() {
        return shiftPressed;
    }

    public void setShiftPressed(boolean shiftPressed) {
        this.shiftPressed.set(shiftPressed);
    }

    public boolean isAltPressed() {
        return altPressed.get();
    }

    public SimpleBooleanProperty altPressedProperty() {
        return altPressed;
    }

    public void setAltPressed(boolean altPressed) {
        this.altPressed.set(altPressed);
    }

    public Object getActionTier() {
        return actionTier.get();
    }

    public SimpleObjectProperty actionTierProperty() {
        return actionTier;
    }

    public void setActionTier(Object actionTier) {
        this.actionTier.set(actionTier);
    }

    public Object getActionImage() {
        return actionImage.get();
    }

    public SimpleObjectProperty actionImageProperty() {
        return actionImage;
    }

    public void setActionImage(Object actionImage) {
        this.actionImage.set(actionImage);
    }

    public Object getActionStyle() {
        return actionStyle.get();
    }

    public SimpleObjectProperty actionStyleProperty() {
        return actionStyle;
    }

    public void setActionStyle(Object actionStyle) {
        this.actionStyle.set(actionStyle);
    }

    public Action(SimpleStringProperty actionName, SimpleIntegerProperty pressedKey, SimpleBooleanProperty ctrlPressed, SimpleBooleanProperty shiftPressed, SimpleBooleanProperty altPressed, SimpleObjectProperty actionTier, SimpleObjectProperty actionImage, SimpleObjectProperty actionStyle) {
        this.actionName = actionName;
        this.pressedKey = pressedKey;
        this.ctrlPressed = ctrlPressed;
        this.shiftPressed = shiftPressed;
        this.altPressed = altPressed;
        this.actionTier = actionTier;
        this.actionImage = actionImage;
        this.actionStyle = actionStyle;
    }
}
