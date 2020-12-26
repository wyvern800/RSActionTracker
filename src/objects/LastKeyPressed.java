package objects;

import sagacity.Main;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

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

    private final boolean controlDown;
    private final boolean shiftDown;
    private final boolean altDown;

    public boolean isControlDown() {
        return controlDown;
    }

    public boolean isShiftDown() {
        return shiftDown;
    }

    public boolean isAltDown() {
        return altDown;
    }

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

    public long getUsedTimeLong() {
        return usedTime.get(ChronoField.MILLI_OF_SECOND);
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
    public LastKeyPressed(LocalTime usedTime, int nativeKeyEvent, boolean controlDown, boolean shiftDown, boolean altDown) {
        this.usedTime = usedTime;
        this.keyCode = nativeKeyEvent;
        this.controlDown = controlDown;
        this.shiftDown = shiftDown;
        this.altDown = altDown;
    }

    public LastKeyPressed(LocalTime usedTime, int nativeKeyEvent) {
        this.usedTime = usedTime;
        this.keyCode = nativeKeyEvent;
        this.controlDown = false;
        this.shiftDown = false;
        this.altDown = false;
    }

}
