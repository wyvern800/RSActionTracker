package objects;

import javafx.scene.paint.Color;

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
        CONSUMABLE(0, "#9500ff"),
            BASIC_ABILITY( 1, "#c96236"),
                TRESHOLD_ABILITY(2, "#1b9400"),
                        ULTIMATE_ABILITY(3, "#ebac00"),
                            DEFENSIVE_ABILITY(3, "white"),
                                    PRAYER( 4, "#007fff"),
                                            WEAPON_SPEC(4, "#bd0026");

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
        private final String abilityBorder;

        /**
         * Gets the ability border color
         * @return The ability border Color
         */
        public String getAbilityBorder() {
            return abilityBorder;
        }

        /**
         * Creates a ability tier
         * @param id The tier Id,
         * @param color The border color
         */
        ActionTier(int id, String color) {
            this.id = id;
            this.abilityBorder = color;
        }
    }
