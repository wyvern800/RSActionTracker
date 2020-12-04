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
        CONSUMABLE(0, Color.BLUEVIOLET),
            BASIC_ABILITY( 1, Color.ORANGE),
                TRESHOLD_ABILITY(2, Color.GREENYELLOW),
                        ULTIMATE_ABILITY(3, Color.LIGHTGOLDENRODYELLOW),
                            DEFENSIVE_ABILITY(3, Color.WHITE),
                                    PRAYER( 4, Color.BLUE);


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
        private final Color abilityBorder;

        /**
         * Gets the ability border color
         * @return The ability border Color
         */
        public Color getAbilityBorder() {
            return abilityBorder;
        }

        /**
         * Creates a ability tier
         * @param id The tier Id,
         * @param color The border color
         */
        ActionTier(int id, Color color) {
            this.id = id;
            this.abilityBorder = color;
        }
    }
