package objects;

import sagacity.Main;

/**
 * Holds the action styles
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 05/12/2020 - 15:38
 * @project RSKeyLogging
 */
public enum ActionStyle {
    NONE(0, "NONE"),
        MELEE(1, "MELEE"),
            RANGED(2, "RANGED"),
                MAGIC(3, "MAGIC");

    /**
     * The action style
     */
    private final int id;

    private final String styleName;

    public String getStyleName() {
        return styleName;
    }

    /**
     * Gets the id of the action style
     * @return the id
     */
    public int getId() {
        return id;
    }
    ;


    /**
     * Wrapper method to get the action Style
     * @param styleName The styleName
     * @return The wrapped style
     */
    public static ActionStyle getWrappedActionStyle(String styleName) {
        for (ActionStyle style : ActionStyle.values()) {
            if (style.name().equals(styleName))
                return style;
        }
        return null;
    }

    /**
     * Creates an attackStyle object
     * @param id The id
     */
    ActionStyle(int id, String styleName) {
        this.id = id;
        this.styleName = styleName;
    }


    /**
     * Generate a default list if the database was deleted / not exist (only used here)
     */
    public static void generateDefaultList() {
        // Load all actions to our cache
        for (ActionStyle store : ActionStyle.values()) {
            if (store == null)
                continue;
            Main.cachedActionStyles.add(store);
        }
    }
}
