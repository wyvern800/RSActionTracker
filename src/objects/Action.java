package objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private final ActionStyle actionStyle;

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
     * @param img The actionImage
     */
    public Action(String actionName, int pressedKey, boolean ctrlPressed, boolean shiftPressed, boolean altPressed, ActionTier tier, Image img, ActionStyle style) {
        this.actionName = actionName;
        this.pressedKey = pressedKey;
        this.ctrlPressed = ctrlPressed;
        this.shiftPressed = shiftPressed;
        this.altPressed = altPressed;
        this.actionImage = new ImageView();
        this.actionTier = tier;
        this.actionStyle = style;
        actionImage.setImage(img);
        actionImage.setFitWidth(104);
        actionImage.setPreserveRatio(true);
        actionImage.setSmooth(true);
        actionImage.setCache(true);
        actionImage.setClip(null); // remove the rounding clip so that our effect can show through.
        actionImage.setPickOnBounds(true);
    }
}
