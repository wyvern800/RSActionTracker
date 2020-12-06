package objects;

import javafx.scene.image.Image;
import org.jnativehook.keyboard.NativeKeyEvent;
import sagacity.Constants;

/**
 * The actions storage
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 04/12/2020 - 14:14
 * @project RSKeyLogging
 */
public enum ActionList implements Constants {
    // Melee rotation
    // Strength
    FURY("Fury", NativeKeyEvent.VC_1, false, false, false,
            ActionTier.BASIC_ABILITY, STRENGTH_PATH+"/fury.png", ActionStyle.MELEE),
    DISMEMBER("Dismember", NativeKeyEvent.VC_2, false, false, false,
            ActionTier.BASIC_ABILITY, STRENGTH_PATH+"/dismember.png", ActionStyle.MELEE),
    QUAKE("Quake", NativeKeyEvent.VC_Q, true, false, false,
            ActionTier.TRESHOLD_ABILITY, STRENGTH_PATH+"/quake.png", ActionStyle.MELEE),
    BERSERK("Berserk", NativeKeyEvent.VC_R, true, false, false,
            ActionTier.ULTIMATE_ABILITY, STRENGTH_PATH+"/berserk.png", ActionStyle.MELEE),
    CLEAVE("Cleave", NativeKeyEvent.VC_4, false,false, false,
            ActionTier.BASIC_ABILITY, STRENGTH_PATH+"/cleave.png", ActionStyle.MELEE),
    KICK("Kick", NativeKeyEvent.VC_3, true,false, false,
            ActionTier.TRESHOLD_ABILITY, STRENGTH_PATH+"/kick.png", ActionStyle.MELEE),
    STOMP("Stomp", NativeKeyEvent.VC_F, true,false, false,
            ActionTier.TRESHOLD_ABILITY, STRENGTH_PATH+"/stomp.png", ActionStyle.MELEE),
    // Attack
    HURRICANE("Hurricane", NativeKeyEvent.VC_W, true,false, false,
            ActionTier.TRESHOLD_ABILITY, ATTACK_PATH+"/hurricane.png", ActionStyle.MELEE),
    SLAUGHTER("Slaughter", NativeKeyEvent.VC_E, true,false, false,
            ActionTier.TRESHOLD_ABILITY, ATTACK_PATH+"/slaughter.png", ActionStyle.MELEE),
    SEVER("Sever", NativeKeyEvent.VC_3, false,false, false,
          ActionTier.BASIC_ABILITY, ATTACK_PATH+"/sever.png", ActionStyle.MELEE),
    SMASH("Smash", NativeKeyEvent.VC_1, true,false, false,
    ActionTier.BASIC_ABILITY, ATTACK_PATH+"/smash.png", ActionStyle.MELEE),
    BARGE("Barge", NativeKeyEvent.VC_A, true,false, false,
            ActionTier.BASIC_ABILITY, ATTACK_PATH+"/barge.png", ActionStyle.MELEE),
    BLOOD_TENDRILS("Blood Tendrils", NativeKeyEvent.VC_U, false,false, false,
            ActionTier.TRESHOLD_ABILITY, ATTACK_PATH+"/tendrils.png", ActionStyle.MELEE),
    TUSKAS_WRATH_1("Tuska's Wrath", NativeKeyEvent.VC_2,true,false, false,
            ActionTier.BASIC_ABILITY, HITPOINTS_PATH+"/tuska_wrath.png", ActionStyle.MELEE),
    SACRIFICE_1("Sacrifice", NativeKeyEvent.VC_4,true,false, false,
            ActionTier.BASIC_ABILITY, HITPOINTS_PATH+"/sacrifice.png", ActionStyle.MELEE),

    // Ranged Rotation
    // 2H
    PIERCING_SHOT("Piercing Shot", NativeKeyEvent.VC_1, false,false, false,
    ActionTier.BASIC_ABILITY, RANGED_PATH+"/piercing_shot.png", ActionStyle.RANGED),
    NEEDLE_STRIKE("Needle Strike", NativeKeyEvent.VC_2, false,false, false,
    ActionTier.BASIC_ABILITY, RANGED_PATH+"/needle_strike.png", ActionStyle.RANGED),
    TUSKAS_WRATH_2("Tuska's Wrath", NativeKeyEvent.VC_3,false,false, false,
            ActionTier.BASIC_ABILITY, HITPOINTS_PATH+"/tuska_wrath.png", ActionStyle.RANGED),
    SNIPE("Snipe", NativeKeyEvent.VC_4, false,false, false,
            ActionTier.BASIC_ABILITY, RANGED_PATH+"/snipe.png", ActionStyle.RANGED),
    SACRIFICE_2("Sacrifice", NativeKeyEvent.VC_1,true,false, false,
            ActionTier.BASIC_ABILITY, HITPOINTS_PATH+"/sacrifice.png", ActionStyle.RANGED),
    RICOCHET("Ricochet", NativeKeyEvent.VC_2,true,false, false,
            ActionTier.BASIC_ABILITY, RANGED_PATH+"/ricochet.png", ActionStyle.RANGED),
    BINDING_SHOT("Binding Shot", NativeKeyEvent.VC_3,true,false, false,
            ActionTier.BASIC_ABILITY, RANGED_PATH+"/binding_shot.png", ActionStyle.RANGED),
    DEADSHOT("Deadshot", NativeKeyEvent.VC_4,true,false, false,
            ActionTier.ULTIMATE_ABILITY, RANGED_PATH+"/deadshot.png", ActionStyle.RANGED),
    SNAP_SHOT("Snap Shot", NativeKeyEvent.VC_Q,true,false, false,
            ActionTier.TRESHOLD_ABILITY, RANGED_PATH+"/snap_shot.png", ActionStyle.RANGED),
    RAPID_FIRE("Rapid Fire", NativeKeyEvent.VC_W,true,false, false,
            ActionTier.TRESHOLD_ABILITY, RANGED_PATH+"/rapid_fire.png", ActionStyle.RANGED),
    BOMBARDMENT("Bombardment", NativeKeyEvent.VC_E,true,false, false,
            ActionTier.TRESHOLD_ABILITY, RANGED_PATH+"/bombardment.png", ActionStyle.RANGED),
    UNLOAD("Unload", NativeKeyEvent.VC_F,true,false, false,
            ActionTier.ULTIMATE_ABILITY, RANGED_PATH+"/unload.png", ActionStyle.RANGED),
    CORRUPTION_SHOT("Corrupt. Shot", NativeKeyEvent.VC_A,true,false, false,
            ActionTier.BASIC_ABILITY, RANGED_PATH+"/corruption_shot.png", ActionStyle.RANGED),
    FRAGMENTATION_SHOT("Frag. Shot", NativeKeyEvent.VC_R,true,false, false,
            ActionTier.BASIC_ABILITY, RANGED_PATH+"/fragmentation_shot.png", ActionStyle.RANGED),

    SHADOW_TENDRILS("Shadow Tendrils", NativeKeyEvent.VC_R, false,false, false,
            ActionTier.TRESHOLD_ABILITY, RANGED_PATH+"/shadow_tendrils.png", ActionStyle.RANGED),
    DEATH_SWIFTNESS("Death's Swiftness", NativeKeyEvent.VC_E, false,false, false,
            ActionTier.ULTIMATE_ABILITY, RANGED_PATH+"/swiftness.png", ActionStyle.RANGED),

    // Defensive abilities
    // Defense
    DEVOTION("Devotion", NativeKeyEvent.VC_V, false,false, false,
            ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/devotion.png", ActionStyle.NONE),
    FREEEDOM("Freedom", NativeKeyEvent.VC_Q, false,false, false,
            ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/freedom.png", ActionStyle.NONE),
    RESONANCE("Resonance", NativeKeyEvent.VC_F2, false,false, false,
            ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/resonance.png", ActionStyle.NONE),
    PROVOKE("Provoke", NativeKeyEvent.VC_F3, false,false, false,
    ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/provoke.png", ActionStyle.NONE),
    PREPARATION("Preparation", NativeKeyEvent.VC_F4, false,false, false,
            ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/preparation.png", ActionStyle.NONE),
    REFLECT("Reflect", NativeKeyEvent.VC_Z, true,false, false,
            ActionTier.TRESHOLD_ABILITY, DEFENSIVE_PATH+"/reflect.png", ActionStyle.NONE),
    DEBILITATE("Debilitate", NativeKeyEvent.VC_D, true,false, false,
            ActionTier.TRESHOLD_ABILITY, DEFENSIVE_PATH+"/debilitate.png", ActionStyle.NONE),
    ANTICIPATION("Anticipation", NativeKeyEvent.VC_W, false,false, false,
            ActionTier.DEFENSIVE_ABILITY, DEFENSIVE_PATH+"/antecipation.png", ActionStyle.NONE),
    BARRICADE("Barricade", NativeKeyEvent.VC_B, false,false, false,
    ActionTier.ULTIMATE_ABILITY, DEFENSIVE_PATH+"/barricade.png", ActionStyle.NONE),

    // Hit points
    ESSENCE_OF_FINALITY("Ess. of Finality", NativeKeyEvent.VC_I,false,false, false,
            ActionTier.WEAPON_SPEC, HITPOINTS_PATH+"/eof.png", ActionStyle.NONE),
    ONSLAUGHT("Onslaught", NativeKeyEvent.VC_P,false,false, false,
            ActionTier.ULTIMATE_ABILITY, HITPOINTS_PATH+"/onslaught.png", ActionStyle.NONE),

    // Prayers
    ANGUISH("Anguish", NativeKeyEvent.VC_Z, false,true, false,
            ActionTier.PRAYER, CURSES_PATH+"/anguish.png", ActionStyle.NONE),
    SOUL_SPLIT("Soul Split", NativeKeyEvent.VC_F, false,false, false,
    ActionTier.PRAYER, CURSES_PATH+"/soulsplit.png", ActionStyle.NONE),
    PROTECT_RANGED("Protect Ranged", NativeKeyEvent.VC_Z, false,false, false,
    ActionTier.PRAYER, CURSES_PATH+"/protect_ranged.png", ActionStyle.NONE),
    PROTECT_MAGIC("Protect Magic", NativeKeyEvent.VC_X, false,false, false,
            ActionTier.PRAYER, CURSES_PATH+"/anti_magic.png", ActionStyle.NONE),
    PROTECT_MELEE("Protect Melee", NativeKeyEvent.VC_C, false,false, false,
            ActionTier.PRAYER, CURSES_PATH+"/anti_melee.png", ActionStyle.NONE),


    // Consumables
    EAT_FOOD("Eat Food", NativeKeyEvent.VC_F5, false, false, false,
            ActionTier.CONSUMABLE, HITPOINTS_PATH+"/eat_a_food.png", ActionStyle.NONE),

    // Items
    REPRISER("Repriser", NativeKeyEvent.VC_F1, false,false, false,
            ActionTier.DEFENSIVE_ABILITY, ITEMS_PATH+"/repriser.png", ActionStyle.NONE),

    // Dashes
    SURGE("Surge", NativeKeyEvent.VC_A, false,false, false,
            ActionTier.BASIC_ABILITY, MAGIC_PATH+"/surge.png", ActionStyle.NONE),
    ESCAPE("Escape", NativeKeyEvent.VC_S, false,false, false,
          ActionTier.BASIC_ABILITY, RANGED_PATH+"/escape.png", ActionStyle.NONE),
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
    ActionList(String actionName, int keyCode, boolean ctrlPressed, boolean shiftPressed, boolean altPressed, ActionTier actionTier, String imgUrl, ActionStyle actionStyle) {
        this.action = new Action(null,actionName, keyCode, ctrlPressed, shiftPressed, altPressed, actionTier, new Image(imgUrl), actionStyle);
    }
}
