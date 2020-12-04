package objects;

import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Represents an action
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 04/12/2020 - 03:36
 * @project RSKeyLogging
 */
public class Action {
    private final String actionName;
    private final int pressedKey;
    private final boolean ctrlPressed;
    private final boolean shiftPressed;
    private final boolean altPressed;
    private final ActionTier actionTier;
    private final ImageView actionImage;

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
     * Gets the action name
     * @return Gets the action name
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Represents an action
     * @param pressedKey The pressedKey
     * @param img The actionImage
     */
    public Action(String actionName, int pressedKey, boolean ctrlPressed, boolean shiftPressed, boolean altPressed, ActionTier tier, Image img) {
        this.actionName = actionName;
        this.pressedKey = pressedKey;
        this.ctrlPressed = ctrlPressed;
        this.shiftPressed = shiftPressed;
        this.altPressed = altPressed;
        this.actionImage = new ImageView();
        this.actionTier = tier;
        actionImage.setImage(img);
        actionImage.setFitWidth(100);
        actionImage.setPreserveRatio(true);
        actionImage.setSmooth(true);
        actionImage.setCache(true);

        // remove the rounding clip so that our effect can show through.
        actionImage.setClip(null);
        actionImage.setPickOnBounds(true);
        // apply a shadow effect.
        actionImage.setEffect(new DropShadow(10, tier.getAbilityBorder()));
        Tooltip tooltip = new Tooltip(actionName);
        Tooltip.install(actionImage, tooltip);
    }
}
