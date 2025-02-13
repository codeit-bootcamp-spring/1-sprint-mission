package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.config.FileConfig;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final String binaryContentJsonFile;
    private final ObjectMapper mapper;
    private Map<UUID, BinaryContent> store;

    @Autowired
    public FileBinaryContentRepository(FileConfig fileConfig) {
        String fileDirectory = fileConfig.getFileDirectory();
        String fileName = fileConfig.getBinaryContentJsonPath();
        this.binaryContentJsonFile = fileDirectory + "/" + fileName;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        store = loadBinaryContentsFromJson();

    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        store = loadBinaryContentsFromJson();
        store.put(binaryContent.getId(), binaryContent);
        saveBinaryContentsToJson(store);
        return binaryContent;
    }
    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(loadBinaryContentsFromJson().get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> binaryContents= new ArrayList<>();
        for (UUID id : ids){
            binaryContents.add(loadBinaryContentsFromJson().get(id));
        }
        return binaryContents;
    }

    @Override
    public boolean existsById(UUID id){
        return loadBinaryContentsFromJson().containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        store = loadBinaryContentsFromJson();
        store.remove(id);
        saveBinaryContentsToJson(store);
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
