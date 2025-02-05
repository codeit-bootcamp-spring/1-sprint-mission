package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.common.BaseEntity;
import com.sprint.mission.discodeit.repository.common.InMemoryCrudRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class FileAbstractRepository<T extends BaseEntity, ID extends UUID>
        extends InMemoryCrudRepository<T, ID> {

    private final File file;

    protected FileAbstractRepository(String filePath) {
        file = new File(filePath);
    }

    protected Map<UUID, T> loadFile() {
        if (!file.exists()) {
            return Collections.emptyMap();
        }

        Map<UUID, T> store = new HashMap<>();
        try (
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            try {
                store = (Map<UUID, T>) ois.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("loading exit");
            throw new IllegalArgumentException();
        }
        return store;
    }


    public void saveToFile() {
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(store);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

