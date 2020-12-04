package objects;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import org.jnativehook.keyboard.NativeKeyEvent;
import sample.Constants;

/**
 * The actions storage
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 04/12/2020 - 14:14
 * @project RSKeyLogging
 */
public enum ActionList implements Constants {
    // Consumables
    EAT_FOOD("Eat Food", NativeKeyEvent.VC_F1, false, false, false,
            ActionTier.CONSUMABLE, HITPOINTS_PATH+"/eat_a_food.png"),

    // Melee abilitiees
    // Strength
    FURY("Fury", NativeKeyEvent.VC_1, false, false, false,
            ActionTier.BASIC_ABILITY, STRENGTH_PATH+"/fury.png"),
    DISMEMBER("Dismember", NativeKeyEvent.VC_2, false, false, false,
            ActionTier.TRESHOLD_ABILITY, STRENGTH_PATH+"/dismember.png"),
    QUAKE("Quake", NativeKeyEvent.VC_Q, true, false, false,
            ActionTier.TRESHOLD_ABILITY, STRENGTH_PATH+"/quake.png"),
    BERSERK("Berserk", NativeKeyEvent.VC_R, true, false, false,
            ActionTier.ULTIMATE_ABILITY, STRENGTH_PATH+"/berserk.png"),

    //Attack
    DESTROY("Destroy", NativeKeyEvent.VC_R, false,false, false,
            ActionTier.TRESHOLD_ABILITY, ATTACK_PATH+"/destroy.png"),
    HURRICANE("Hurricane", NativeKeyEvent.VC_W, true,false, false,
            ActionTier.TRESHOLD_ABILITY, ATTACK_PATH+"/hurricane.png"),
    SLAUGHTER("Slaughter", NativeKeyEvent.VC_E, true,false, false,
            ActionTier.TRESHOLD_ABILITY, ATTACK_PATH+"/slaughter.png"),

    // Defensive abilities
    // Defense
    DEVOTION("Devotion", NativeKeyEvent.VC_V, false,false, false,
            ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/devotion.png"),
    FREEEDOM("Freedom", NativeKeyEvent.VC_Q, false,false, false,
            ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/freedom.png"),
    RESONANCE("Resonance", NativeKeyEvent.VC_F2, false,false, false,
            ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/resonance.png"),
    PROVOKE("Provoke", NativeKeyEvent.VC_F3, false,false, false,
    ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/provoke.png"),
    PREPARATION("Preparation", NativeKeyEvent.VC_F4, false,false, false,
            ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/preparation.png"),
    REFLECT("Reflect", NativeKeyEvent.VC_Z, true,false, false,
            ActionTier.TRESHOLD_ABILITY, DEFENSIVE_PATH+"/reflect.png"),
    DEBILITATE("Debilitate", NativeKeyEvent.VC_D, true,false, false,
            ActionTier.TRESHOLD_ABILITY, DEFENSIVE_PATH+"/debilitate.png"),
    ANTICIPATION("Anticipation", NativeKeyEvent.VC_W, false,false, false,
            ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/antecipation.png"),
    BARRICADE("Barricade", NativeKeyEvent.VC_B, false,false, false,
    ActionTier.ULTIMATE_ABILITY, DEFENSIVE_PATH+"/barricade.png"),

    // Hit points
    TUSKAS_WRATH("Tuska's Wrath", NativeKeyEvent.VC_3,false,false, true,
            ActionTier.BASIC_ABILITY, HITPOINTS_PATH+"/tuska_wrath.png"),

    // Prayers
    ANGUISH("Anguish", NativeKeyEvent.VC_Z, false,true, false,
            ActionTier.PRAYER, CURSES_PATH+"/anguish.png"),
    SOULSPLIT("Soul Split", NativeKeyEvent.VC_F, false,false, false,
    ActionTier.PRAYER, CURSES_PATH+"/soulsplit.png"),
    PROTECT_RANGED("Protect Rangedd", NativeKeyEvent.VC_Z, false,false, false,
    ActionTier.PRAYER, CURSES_PATH+"/protect_ranged.png"),
    PROTECT_MAGIC("Protect Magic", NativeKeyEvent.VC_X, false,false, false,
            ActionTier.PRAYER, CURSES_PATH+"/anti_magic.png"),
    PROTECT_MELEE("Protect Melee", NativeKeyEvent.VC_C, false,false, false,
            ActionTier.PRAYER, CURSES_PATH+"/anti_melee.png"),


    // Magic
    SURGE("Surge", NativeKeyEvent.VC_A, false,false, false,
            ActionTier.BASIC_ABILITY, MAGIC_PATH+"/surge.png"),

    // Ranged
    ESCAPE("Escape", NativeKeyEvent.VC_S, false,false, false,
          ActionTier.BASIC_ABILITY, RANGED_PATH+"/escape.png")
    ;

    /**
     * The action
     */
    private final Action action;

    /**
     * Gets the action
     * @return The action
     */
    public Action getAction() {
        return action;
    }
    ;

    /**
     * Creates a instance of the action in the list
     * @param actionName The action name
     * @param keyCode The keyCod
     * @param actionTier The actionTier
     * @param imgUrl the imgUrl
     */
    ActionList(String actionName, int keyCode, boolean ctrlPressed, boolean shiftPressed, boolean altPressed, ActionTier actionTier, String imgUrl) {
        this.action = new Action(actionName, keyCode, ctrlPressed, shiftPressed, altPressed, actionTier, new Image(imgUrl));
    }
}
