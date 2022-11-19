package boardgame;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;

public class GameStorage {
    public static void save(Saveable saveObj, Path path) throws IOException{
        String saveStr = saveObj.getStringToSave();
        BufferedWriter writer = Files.newBufferedWriter(path);
        writer.write(saveStr);
        writer.close();
    }

    public static void load(Saveable loadObj, Path path) throws IOException{
        List<String> lines = Files.readAllLines(path);
        String loadStr = String.join("\n", lines) + "\n";
        loadObj.loadSavedString(loadStr);
    }
}
