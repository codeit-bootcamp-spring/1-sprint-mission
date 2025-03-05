package com.sprint.mission.discodeit.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;
import java.util.Map;

public class FileUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void saveToFile(String filePath, Object data) {
        try (FileWriter writer = new FileWriter(filePath)) {
            objectMapper.writeValue(writer, data); // JSON 형식으로 저장
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + filePath, e);
        }
    }

    public static <T> T loadFromFile(String filePath, TypeReference<T> typeReference) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        try (FileReader reader = new FileReader(filePath)) {
            return objectMapper.readValue(reader, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("파일 로드 중 오류 발생: " + filePath, e);
        }
    }
}
