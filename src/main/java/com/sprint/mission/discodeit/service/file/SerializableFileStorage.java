package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.service.FileStorage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SerializableFileStorage<T> implements FileStorage<T> {
    private final Class<T> type;

    public SerializableFileStorage(Class<T> type) {
        this.type = type;
    }

    @Override
    public void init(Path directory) {
        if(!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void save(Path filePath, List<T> data) {
        // Set을 사용하여 중복 제거
        Set<T> uniqueData = new LinkedHashSet<>(data);
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(new ArrayList<>(uniqueData));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> load(Path directory) {
        Path filePath = directory.resolve(type.getSimpleName().toLowerCase() + ".ser");
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath.toFile()))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
