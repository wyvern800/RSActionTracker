package objects;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import sagacity.Main;

/**
 * Represents an action tier
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 04/12/2020 - 03:37
 * @project RSKeyLogging
 */

    /**
     * The actions tier storage
     */
    public enum ActionTier {
        CONSUMABLE(0, "CONSUMABLE", "#9500ff"),
            BASIC_ABILITY( 1, "BASIC_ABILITY", "#c96236"),
                TRESHOLD_ABILITY(2, "TRESHOLD_ABILITY", "#1b9400"),
                        ULTIMATE_ABILITY(3, "ULTIMATE_ABILITY", "#ebac00"),
                            DEFENSIVE_ABILITY(3, "DEFENSIVE_ABILITY", "white"),
                                    PRAYER( 4, "PRAYER", "#007fff"),
                                            WEAPON_SPEC(4, "WEAPON_SPEC", "#bd0026");

        /**
         * The action id
         */
        private final int id;

        /**
         * Gets the action id
         * @return The action id
         */
        public int getId() {
            return id;
        }

        /**
         * The ability border color
         */
        private String abilityBorder;
        private final String tierName;
        private transient ColorPicker colorPicker;

        public ColorPicker getColorPicker() {
            return colorPicker;
        }

        public void setColorPicker(ColorPicker colorPicker) {
            this.colorPicker = colorPicker;
        }

        public void setAbilityBorder(String abilityBorder) {
            this.abilityBorder = abilityBorder;
        }

        public String getTierName() {
            return tierName;
        }

        /**
         * Gets the ability border color
         * @return The ability border Color
         */
        public String getAbilityBorder() {
            return abilityBorder;
        }

        /**
         * Gets the wrapped action tier
         * @param tierName The tierName
         * @return The wrapped tierName
         */
        public static ActionTier getWrappedActionTier(String tierName) {
            for (ActionTier tier : ActionTier.values()) {
                if (tier.name().equals(tierName))
                    return tier;
            }
            return null;
        }

        /**
         * Creates a ability tier
         * @param id The tier Id,
         * @param color The border color
         */
        ActionTier(int id, String tierName, String color) {
            this.id = id;
            this.abilityBorder = color;
            this.tierName = tierName;
            this.colorPicker = new ColorPicker(Color.web(abilityBorder));
            colorPicker.setDisable(true);
        }


        /**
         * Generate a default list if the database was deleted / not exist (only used here)
         */
        public static void generateDefaultList() {
            // Load all actions to our cache
            for (ActionTier store : ActionTier.values()) {
                if (store == null)
                    continue;
                Main.cachedActionTiers.add(store);
            }
        }
    }
