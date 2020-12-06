package sagacity;

/**
 * Some constants used within the app
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 04/12/2020 - 04:33
 * @project RSKeyLogging
 */
public interface Constants {
    // Show debug messages while executing
    boolean isDebugMode = false;

    // Show ability names on screen
    boolean showAbilityName = true;

    String STRENGTH_PATH = "resources/abilities/melee/strength";
    String ATTACK_PATH = "resources/abilities/melee/attack";
    String HITPOINTS_PATH = "resources/abilities/hitpoints";
    String DEFENSIVE_PATH = "resources/abilities/defensive";
    String CURSES_PATH = "resources/prayers/curses";
    String MAGIC_PATH = "resources/abilities/magic";
    String RANGED_PATH = "resources/abilities/ranged";
    String ITEMS_PATH = "resources/abilities/items";

    String[] TOTAl_ACTIONS = {"src/logs", "totalActions.txt"};
}
