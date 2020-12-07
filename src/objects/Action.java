package objects;
import javafx.scene.control.TableView;
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
    //private TableView tableView;
    private String actionName;
    private int pressedKey;
    private boolean ctrlPressed;
    private boolean shiftPressed;
    private boolean altPressed;
    private ActionTier actionTier;
    private transient ImageView actionImage;
    private ActionStyle actionStyle;

    /*public TableView getTableView() {
        return tableView;
    }*/

    /*public void setTableView(TableView tableView) {
        this.tableView = tableView;
    }*/

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
    public Action(/*TableView table, */String actionName, int pressedKey, boolean ctrlPressed, boolean shiftPressed, boolean altPressed, ActionTier tier, Image img, ActionStyle style) {
        //this.tableView = table;
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
