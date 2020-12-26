package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * File writter used to write things out
 *
 * @author wyvern800 - http://github.com/wyvern800
 * @created 04/12/2020 - 16:19
 * @project RSActionTracker
 */
public class FileWritter {

    /**
     * Write something inside a file
     * @param path The path of the file
     * @param fileName The filename
     * @param whatToWrite Array of what to write in that file
     */
    public static void write(String path, String fileName, String[] whatToWrite) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path+"/"+fileName), StandardCharsets.UTF_8))) {
            for (String s : whatToWrite) {
                writer.write(s);
            }
        } catch (IOException ex) {
            // Report
            System.out.println("Error while writting to file: "+fileName);
        }
    }
}
