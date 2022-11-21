package boardgame;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;

/**
 * Class providing static methods from saving and loading a Saveable object to/from a Path.
 */
public class GameStorage {
    /**
     * Saves saveObj data to path.
     * @param saveObj Saveable object to save
     * @param path to save
     * @throws IOException
     */
    public static void save(Saveable saveObj, Path path) throws IOException{
        String saveStr = saveObj.getStringToSave();
        BufferedWriter writer = Files.newBufferedWriter(path);
        writer.write(saveStr);
        writer.close();
    }

    /**
     * Loads data into loadObj from path
     * @param loadObj Saveable object to load to
     * @param path to load
     * @throws IOException
     */
    public static void load(Saveable loadObj, Path path) throws IOException{
        List<String> lines = Files.readAllLines(path);
        String loadStr = String.join("\n", lines) + "\n";
        loadObj.loadSavedString(loadStr);
    }
}
