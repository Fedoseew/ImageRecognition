package Logic;

import Config.DB_TABLES;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;


public class InsertScriptsFileUtils {

    public static List<String> readInsertScriptsFile() throws IOException {

        return Files.readAllLines(
                Paths.get("src/main/resources/db/insert.sql"), StandardCharsets.UTF_8);
    }

    public static void deleteSourceFromInsertScriptsFile(String source, DB_TABLES table) throws IOException {

        String deletedRow = "INSERT INTO " + table + " VALUES (" + source + ");";
        List<String> lines = readInsertScriptsFile();
        lines.remove(deletedRow);
        Files.write(Paths.get("src/main/resources/db/insert.sql"), lines);
    }

    public static void writeInsertScriptsFile(String source, DB_TABLES table) throws IOException {

        String SQL = "INSERT INTO " + table + " VALUES (" + source + ");";
        StringBuilder oldScripts = new StringBuilder();

        for(String row : readInsertScriptsFile()) {
            if(row.contains(table.toString())) {
                oldScripts.append("--UPDATE FROM ").append(LocalDateTime.now()).append("--\n");
                oldScripts.append(SQL).append("\n\n");
            }
            oldScripts.append(row).append("\n");
        }

        Files.write(
                Paths.get("src/main/resources/db/insert.sql"),
                oldScripts.toString().getBytes(StandardCharsets.UTF_8)
        );
    }
}
