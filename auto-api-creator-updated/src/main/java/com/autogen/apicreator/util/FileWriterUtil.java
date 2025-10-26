package com.autogen.apicreator.util;

import java.io.IOException;
import java.nio.file.*;

public class FileWriterUtil {

    public static void writeToFile(String relativePath, String content) {
        try {
            Path path = Paths.get(relativePath);
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(path, content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: " + relativePath, e);
        }
    }
}
