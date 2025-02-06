package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.FileConfig;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
@Primary
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final String binaryContentJsonFile;
    private final ObjectMapper mapper;
    private Map<UUID, BinaryContent> store;

    @Autowired
    public FileBinaryContentRepository(FileConfig fileConfig) {
        this.binaryContentJsonFile = fileConfig.getBinaryContentJsonPath();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        store = loadBinaryContentsFromJson();

    }

    @Override
    public Optional<BinaryContent> save(BinaryContent binaryContent) {
        store.put(binaryContent.getId(), binaryContent);
        saveBinaryContentsToJson(store);
        return Optional.of(binaryContent);
    }

    @Override
    public Optional<BinaryContent> findProfileByUserId(UUID userId) {
        return loadBinaryContentsFromJson().values().stream()
                .filter(content -> content.getUserId().equals(userId) && content.getMessageId() == null)
                .findFirst();
    }

    @Override
    public Optional<BinaryContent> findByContentId(UUID contentId) {
        return loadBinaryContentsFromJson().values().stream()
                .filter(content -> content.getId().equals(contentId))
                .findFirst();
    }

    @Override
    public List<BinaryContent> findAllByUserId(UUID userId) {
        return new ArrayList<>(loadBinaryContentsFromJson().values().stream()
                .filter(content -> content.getUserId().equals(userId))
                .toList()
        );
    }

    @Override
    public List<BinaryContent> findByAllMessageId(UUID messageId) {
        return new ArrayList<>(loadBinaryContentsFromJson().values().stream()
                .filter(content -> content.getMessageId().equals(messageId))
                .toList()
        );
    }

    @Override
    public void deleteByUserId(UUID userId) {
        store = loadBinaryContentsFromJson();
        if (store.containsKey(userId)){
            store.remove(userId);
            saveBinaryContentsToJson(store);
        }
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        store = loadBinaryContentsFromJson();
        if (store.containsKey(messageId)){
            store.remove(messageId);
            saveBinaryContentsToJson(store);
        }
    }

    @Override
    public void deleteByContentId(UUID contentId) {
        store = loadBinaryContentsFromJson();
        if (store.containsKey(contentId)){
            store.remove(contentId);
            saveBinaryContentsToJson(store);
        }
    }

    private Map<UUID, BinaryContent> loadBinaryContentsFromJson() {
        Map<UUID, BinaryContent> map = new HashMap<>();
        File file = new File(binaryContentJsonFile);
        if (!file.exists()) {
            return map;
        }
        try {
            map = mapper.readValue(file, new TypeReference<Map<UUID, BinaryContent>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
        return map;
    }

    private void saveBinaryContentsToJson(Map<UUID, BinaryContent> store) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(binaryContentJsonFile), store);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
    }
}
