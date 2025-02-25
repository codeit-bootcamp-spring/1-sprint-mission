package com.sprint.mission.discodeit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileIOUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule()); // Instant, UUID 변환 지원
    }

    public static <T> Map<UUID, T> loadFromFile(Path filePath) {
        if (!Files.exists(filePath)) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Map<UUID, T>) ois.readObject();
        } catch (EOFException e) {
            return new HashMap<>(); // 파일이 비어 있을 경우 빈 맵 반환
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("❌ 파일을 읽는 도중 오류 발생 (" + filePath + "): " + e.getMessage(), e);
        }
    }

    public static <T> void saveToFile(Map<UUID, T> data, Path filePath) {
        try {
            Files.createDirectories(filePath.getParent()); // 디렉토리 없으면 생성
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("❌ 파일 저장 중 오류 발생 (" + filePath + "): " + e.getMessage(), e);
        }
    }

    // `.dat` 파일을 읽기 쉬운 JSON 파일로 변환
    public static <T> void convertSerToJson(Path datFilePath, Path jsonFilePath, Class<T> valueType) {
        Map<UUID, T> data = loadFromFile(datFilePath);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFilePath.toFile(), data);
        } catch (IOException e) {
            throw new RuntimeException("❌ JSON 변환 중 오류 발생 (" + jsonFilePath + "): " + e.getMessage(), e);
        }
    }
}
