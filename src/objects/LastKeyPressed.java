package objects;

import java.time.LocalTime;

/**
 * Holds the last key we pressed
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 05/12/2020 - 21:10
 * @project RSKeyLogging
 */
public class LastKeyPressed {
    /**
     * When we last pressed our key
     */
    private final LocalTime usedTime;

    /**
     * The nativeEvent key we used
     */
    private final int keyCode;

    /**
     * Gets the usedTime
     * @return The usedTime
     */
    public LocalTime getUsedTime() {
        return usedTime;
    }

    /**
     * Gets the nativeKeyEvent
     * @return The nativeKeyEvent
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Creates a LastKeyPressed object
     * @param usedTime The time we last pressed the key
     * @param nativeKeyEvent The key we pressed
     */
    public LastKeyPressed(LocalTime usedTime, int nativeKeyEvent) {
        this.usedTime = usedTime;
        this.keyCode = nativeKeyEvent;
    }
}
