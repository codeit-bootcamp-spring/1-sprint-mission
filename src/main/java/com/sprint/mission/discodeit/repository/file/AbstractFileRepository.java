package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BaseEntity;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractFileRepository<T extends BaseEntity> {
    protected final Map<UUID, T> data;
    private final String FILE_PATH;

    public AbstractFileRepository(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
        this.data = loadData();
    }

    public T save(T entity) {
        data.put(entity.getId(), entity);
        saveData();
        return entity;
    }

    public Optional<T> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }


    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public boolean delete(UUID id) {
        boolean deleted = data.remove(id) != null;
        if (deleted) {
            saveData();
        }
        return deleted;
    }

    private Map<UUID, T> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ConcurrentHashMap<>();
        }
    }

    protected void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
