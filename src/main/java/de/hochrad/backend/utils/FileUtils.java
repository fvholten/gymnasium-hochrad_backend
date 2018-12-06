package de.hochrad.backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

class FileUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FILE_PATH = "\\tmp\\";


    static void writeObjectToFile(Object object, String filename) {
        File file = new File(FILE_PATH + filename + ".json");
        try {
            objectMapper.writeValue(file, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Optional getObjectFromFile(String filename, Class<?> tClass) {
        File file = new File(FILE_PATH + filename + ".json");

        if (file.exists()) {
            try {
                return Optional.of(objectMapper.readValue(file, tClass));
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

}
