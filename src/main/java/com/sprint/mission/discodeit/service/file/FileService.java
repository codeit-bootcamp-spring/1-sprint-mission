package com.sprint.mission.discodeit.service.file;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface FileService<T> {

    default void save(String path, Map<UUID, T> map) {
        try (FileOutputStream fos = new FileOutputStream(path, false);
             ObjectOutputStream out = new ObjectOutputStream(fos)) {

            out.writeObject(map);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default Map<UUID, T> load(String path, Map<UUID, T> map) {

        Map<UUID, T> newMap = new HashMap<>();

        try (FileInputStream fos = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fos)) {

            newMap = (HashMap<UUID, T>) ois.readObject();

        } catch (FileNotFoundException e) {
            return map;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return newMap;
    }
}
