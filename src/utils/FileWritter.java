package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Default header
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 04/12/2020 - 16:19
 * @project RSKeyLogging
 */
public class FileWritter {
    public static void write(int totalActions) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("totalActions.txt"), StandardCharsets.UTF_8))) {
            writer.write(""+totalActions);
        } catch (IOException ex) {
            // Report
        }
        /*ignore*/
    }
}
