package objects;

/**
 * Holds the action styles
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 05/12/2020 - 15:38
 * @project RSKeyLogging
 */
public enum ActionStyle {
    NONE(0),
        MELEE(1),
            RANGED(2),
                MAGIC(3);

    /**
     * The action style
     */
    private final int id;

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
    ActionStyle(int id) {
        this.id = id;
    }
}
