package utils;

import javafx.stage.FileChooser;
import sagacity.Constants;
import sagacity.Main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default header
 *
 * @author wyvern800 - http://github.com/wyvern800
 * @created 07/12/2020 - 15:14
 * @project RSActionTracker
 */
public class FileManager implements Constants {
    public static void configureFileChooser(final FileChooser fileChooser, String title) {
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(RESOURCES_PATH));
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

        /**
         * Opens a file
         * @param file The file to be opened
         */
    public static void openFile(File file) {
        try {
            Main.desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    Main.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }
}
