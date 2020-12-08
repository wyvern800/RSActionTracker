package objects;

import sagacity.Constants;
import sagacity.Main;
import utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Default header
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
        this.cachedActions = new ArrayList<>(Main.cachedActions);
    }

    /**
     * Saves the information to the database
     */
    public void saveData() {
        GsonUtil.save(Main.getSavedData(), SavedData.SAVINGS_PATH, SavedData.class);
    }
}
