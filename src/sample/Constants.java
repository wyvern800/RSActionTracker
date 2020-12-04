package sample;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import objects.Action;
import objects.ActionTier;

/**
 * Default header
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 04/12/2020 - 04:33
 * @project RSKeyLogging
 */
public interface Constants {
    String STRENGTH_PATH = "resources/abilities/melee/strength";
    String ATTACK_PATH = "resources/abilities/melee/attack";
    String HITPOINTS_PATH = "resources/abilities/hitpoints";
    String DEFENSIVE_PATH = "resources/abilities/defensive";
    String CURSES_PATH = "resources/prayers/curses";
    String MAGIC_PATH = "resources/abilities/magic";
    String RANGED_PATH = "resources/abilities/ranged";


    int CTRL_L_MASK			= 1 << 1;
    int CTRL_R_MASK			= 1 << 5;
    int CTRL_MASK			= CTRL_L_MASK  | CTRL_R_MASK;

    int SHIFT_L_MASK		= 1 << 0;
    int SHIFT_R_MASK		= 1 << 4;
    int SHIFT_MASK			= SHIFT_L_MASK | SHIFT_R_MASK;

    int ALT_L_MASK			= 1 << 3;
    int ALT_R_MASK			= 1 << 7;
    int ALT_MASK			= ALT_L_MASK   | ALT_R_MASK;
}
