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
    public static final String SAVINGS_PATH = "data/database.json";

    /**
     * Cached actions
     */
    private List<Action> cachedActions;


    public List<Action> getCachedActions() {
        return cachedActions;
    }


    public void setCachedActions(List<Action> cachedActions) {
        this.cachedActions = cachedActions;
        save();
    }

    public SavedData() {
        this.cachedActions = new ArrayList<>(Main.cachedActions);
    }

    public void save() {
        GsonUtil.save(Main.getSavedData(), SavedData.SAVINGS_PATH, SavedData.class);
    }
}
