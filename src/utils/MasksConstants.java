package utils;

/**
 * Used for CTRL, SHIFT, ALT masks, do not touch
 *
 * @author wyvern800 - http://github.com/wyvern800
 * @created 05/12/2020 - 21:55
 * @project RSActionTracker
 */
public interface MasksConstants {
    // Do not touch
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
