package com.sprint.mission.discodeit.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileIOUtil {

    public static <T> Map<UUID, T> loadFromFile(Path filePath) {
        if (!Files.exists(filePath)) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Map<UUID, T>) ois.readObject();
        } catch (EOFException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일을 읽어오는 도중 문제가 발생하였습니다.", e);
        }
    }

    public static <T> void saveToFile(Map<UUID, T> data, Path filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 도중 문제가 발생하였습니다.", e);
        }
    }

    //.dat 파일을 읽기 쉬운 Json 파일로 변환하는 메서드
    public static void convertDSerToJson(Path datFilePath, Path jsonFilePath) {
        Map<UUID, Object> data = loadFromFile(datFilePath);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFilePath.toFile(), data);
        } catch (IOException e) {
            throw new RuntimeException("JSON 파일로 변환하는 도중 문제가 발생하였습니다.", e);
        }
    }
}