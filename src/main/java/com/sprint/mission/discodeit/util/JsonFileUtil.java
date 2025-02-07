package com.sprint.mission.discodeit.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class JsonFileUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    public static <T> T loadFromJson(String filePath, TypeReference<T> typeReference) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            return mapper.readValue(file, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from JSON file.", e);
        }
    }

    public static <T> void saveToJson(String filePath, T data) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to JSON file.", e);
        }
    }
}
