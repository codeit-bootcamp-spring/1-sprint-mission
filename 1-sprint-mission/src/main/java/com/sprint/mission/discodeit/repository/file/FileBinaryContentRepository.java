package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private static final String FILE_PATH = "tmp/binary_content.ser";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<UUID, BinaryContent>binaryContentData;

    public FileBinaryContentRepository() {
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

    private Map<UUID, BinaryContent> loadFromFile() {
        File file = new File(FILE_PATH);
        if(!file.exists()){
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<Map<UUID, BinaryContent>>() {});
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new HashMap<>();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(binaryContentData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save binary content data to file.", e);
        }
    }
}
