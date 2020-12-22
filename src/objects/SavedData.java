package objects;

import sagacity.Constants;
import sagacity.Main;
import utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the DTO
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 07/12/2020 - 02:19
 * @project RSKeyLogging
 */
public class SavedData implements Constants {
    public static final String SAVINGS_PATH = DATA_PATH+"database.json";

    /**
     * Cached actions
     */
    private List<Action> cachedActions;
    private List<ActionTier> cachedActionTiers;
    private List<ActionStyle> cachedActionStyles;
    private ActionStyle actionStyle;
    private boolean showActionName;

    public boolean isShowActionName() {
        return showActionName;
    }

    public void setShowActionName(boolean showActionName) {
        this.showActionName = showActionName;
        saveData();
    }

    public ActionStyle getActionStyle() {
        return actionStyle;
    }

    public void setActionStyle(ActionStyle actionStyle) {
        this.actionStyle = actionStyle;
        saveData();
    }

    public List<ActionStyle> getCachedActionStyles() {
        return cachedActionStyles;
    }

    public void setCachedActionStyles(List<ActionStyle> cachedActionStyles) {
        this.cachedActionStyles = cachedActionStyles;
        saveData();
    }

    /**
     * Gets the cached actions
     * @return The cachedActions
     */
    public List<Action> getCachedActions() {
        return cachedActions;
    }


    /**
     * Sets the cachedActions
     * @param cachedActions The cachedActions
     */
    public void setCachedActions(List<Action> cachedActions) {
        this.cachedActions = cachedActions;
        saveData();
    }

    /**
     * Creates the object
     */
    public SavedData() {
        ActionList.generateDefaultList();
        ActionTier.generateDefaultList();
        ActionStyle.generateDefaultList();
        this.cachedActions = new ArrayList<>(Main.cachedActions);
        this.cachedActionTiers = new ArrayList<>(Main.cachedActionTiers);
        this.cachedActionStyles = new ArrayList<>(Main.cachedActionStyles);
        this.actionStyle = ActionStyle.RANGED;
        this.showActionName = true;
    }

    /**
     * Gets the cached action tiers
     * @return The cachedActionTier
     */
    public List<ActionTier> getCachedActionTiers() {
        return cachedActionTiers;
    }

    public void setCachedActionTiers(List<ActionTier> cachedActionTiers) {
        this.cachedActionTiers = cachedActionTiers;
        saveData();
    }

    /**
     * Saves the information to the database
     */
    public void saveData() {
        GsonUtil.save(Main.getSavedData(), SavedData.SAVINGS_PATH, SavedData.class);
    }
}
