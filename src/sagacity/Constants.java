package sagacity;

/**
 * Some constants used within the app
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 04/12/2020 - 04:33
 * @project RSKeyLogging
 */
public interface Constants {
    String DATA_PATH = "./data/";
    String RESOURCES_PATH = "./data/resources/";

    String STRENGTH_PATH = RESOURCES_PATH+"abilities/melee/strength";
    String ATTACK_PATH = RESOURCES_PATH+"abilities/melee/attack";
    String HITPOINTS_PATH = RESOURCES_PATH+"abilities/hitpoints";
    String DEFENSIVE_PATH = RESOURCES_PATH+"abilities/defensive";
    String CURSES_PATH = RESOURCES_PATH+"prayers/curses";
    String MAGIC_PATH = RESOURCES_PATH+"abilities/magic";
    String RANGED_PATH = RESOURCES_PATH+"abilities/ranged";
    String ITEMS_PATH = RESOURCES_PATH+"abilities/items";

    String[] TOTAl_ACTIONS = {"./data/logs", "totalActions.txt"};

    String ICONS_PATH = "resources/icons";
}
