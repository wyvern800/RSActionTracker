package objects;

/**
 * Default header
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

    private final int id;

    public int getId() {
        return id;
    }
    ;

    ActionStyle(int id) {
        this.id = id;
    }
}
