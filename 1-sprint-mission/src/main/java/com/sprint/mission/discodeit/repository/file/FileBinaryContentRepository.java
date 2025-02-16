package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.interfacepac.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class FileBinaryContentRepository implements BinaryContentRepository {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String filePath;
    private final Map<UUID, BinaryContent> binaryContentData;

    public FileBinaryContentRepository(@Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        if(!fileDirectory.endsWith("/")) {
            fileDirectory += "/";
        }
        this.filePath = fileDirectory + "binary_content.json";

        ensureDirectoryExists(this.filePath);
        this.binaryContentData = loadFromFile();
    }

    @Override
    public void save(BinaryContent binaryContent) {
        binaryContentData.put(binaryContent.getId(), binaryContent);
        saveToFile();
    }

    @Override
    public List<BinaryContent> findAllByUserId(UUID userId) {
        return binaryContentData.values().stream()
                .filter(content -> userId.equals(content.getUserId()))
                .toList();
    }

    @Override
    public List<BinaryContent> findAllByMessageId(UUID messageId) {
        return binaryContentData.values().stream()
                .filter(content -> messageId.equals(content.getMessageId()))
                .toList();
    }

    @Override
    public Optional<BinaryContent> findById(UUID binaryContentId) {
        return Optional.ofNullable(binaryContentData.get(binaryContentId));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentData.values().stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }

    @Override
    public void delete(BinaryContent binaryContent) {
        binaryContentData.remove(binaryContent.getId());
        saveToFile();
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        binaryContentData.values().removeIf(content -> messageId.equals(content.getMessageId()));
        saveToFile();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        binaryContentData.values().removeIf(content -> userId.equals(content.getUserId()));
        saveToFile();
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return binaryContentData.values().stream()
                .anyMatch(content -> userId.equals(content.getUserId()));
    }


    private void ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        File parentDirectory = directory.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }

    private Map<UUID, BinaryContent> loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try {
            return objectMapper.readValue(file,
                    new TypeReference<Map<UUID, BinaryContent>>() {});
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }

    private void saveToFile() {
        File file = new File(filePath);
        try {
            objectMapper.writeValue(file, binaryContentData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save binary content data.", e);
        }
    }

}
