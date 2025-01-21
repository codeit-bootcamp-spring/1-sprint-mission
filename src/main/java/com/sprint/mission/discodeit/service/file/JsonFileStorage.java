package com.sprint.mission.discodeit.service.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.service.FileStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonFileStorage<T> implements FileStorage<T> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public final Class<T> type;
    private final TypeReference<List<T>> listType = new TypeReference<>() {};

    public JsonFileStorage(Class<T> type) {
        this.type = type;
    }


    @Override
    public void init(Path directory) {
        if(!Files.exists(directory)) {
            try{
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void save(Path filePath, List<T> data) {
        try{
            objectMapper.writeValue(filePath.toFile(), data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> load(Path directory) {
        Path filePath = directory.resolve(type.getSimpleName().toLowerCase() + ".json");
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(filePath.toFile(), listType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public T loadFile(Path directory) {
        try{
            return objectMapper.readValue(directory.toFile(), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
