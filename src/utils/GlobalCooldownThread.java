package utils;

import objects.LastKeyPressed;
import org.jnativehook.keyboard.NativeKeyEvent;
import sagacity.Main;

import java.time.LocalTime;

/**
 * Default header
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 05/12/2020 - 22:38
 * @project RSKeyLogging
 */
public class GlobalCooldownThread extends Thread {
    public void run() {
        System.out.println("Global cooldown thread started");
            try {
                while (true) {
                    Main.lastKeyPressed = new LastKeyPressed(LocalTime.now(), NativeKeyEvent.VC_F11);
                    sleep( 5000);
                    System.out.println("Global cooldown resetted");
                }
            } catch (InterruptedException e) {
            }
    }
}
