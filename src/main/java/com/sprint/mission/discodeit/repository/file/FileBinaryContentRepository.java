package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Primary
public class FileBinaryContentRepository implements BinaryContentRepository {

    private static final String BINARY_CONTENT_JSON_FILE = "tmp/binaryContents.json";
    private final ObjectMapper mapper;
    private Map<UUID, BinaryContent> store;

    public FileBinaryContentRepository() {
        mapper = new ObjectMapper();
        store = loadBinaryContentsFromJson();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        store.put(binaryContent.getId(), binaryContent);
        saveBinaryContentsToJson(store);
        return binaryContent;
    }

    @Override
    public BinaryContent findByUserIdAndMessageId(UUID userId, UUID messageId) {
        return store.values().stream()
                .filter(content -> content.getUserId().equals(userId) && content.getMessageId().equals(messageId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public BinaryContent findByUserId(UUID userId) {
        return store.values().stream()
                .filter(content -> content.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public BinaryContent findByContentId(UUID contentId) {
        return store.get(contentId);
    }

    @Override
    public List<BinaryContent> findAllByContentId(List<UUID> ids) {
        return store.values().stream()
                .filter(content -> ids.contains(content.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByUserId(UUID userId) {
        store.values().removeIf(content -> content.getUserId().equals(userId));
        saveBinaryContentsToJson(store);
    }

    @Override
    public void deleteByContentId(UUID contentId) {
        store.values().removeIf(content -> content.getId().equals(contentId));
        saveBinaryContentsToJson(store);
    }

    private Map<UUID, BinaryContent> loadBinaryContentsFromJson() {
        Map<UUID, BinaryContent> map = new HashMap<>();
        File file = new File(BINARY_CONTENT_JSON_FILE);
        if (!file.exists()) {
            return map;
        }
        try {
            map = mapper.readValue(file, new TypeReference<Map<UUID, BinaryContent>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void saveBinaryContentsToJson(Map<UUID, BinaryContent> store) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(BINARY_CONTENT_JSON_FILE), store);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
