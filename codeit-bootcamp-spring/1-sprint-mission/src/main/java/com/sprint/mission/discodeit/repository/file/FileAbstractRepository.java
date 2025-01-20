package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import com.sprint.mission.discodeit.repository.common.InMemoryCrudRepository;
import java.io.Closeable;
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

public abstract class FileAbstractRepository<T extends AbstractUUIDEntity, ID extends UUID>
        extends InMemoryCrudRepository<T, ID>
        implements Closeable {

    private File file;

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
            while (true) {
                try {
                    var readUser = (T) ois.readObject();
                    store.put(readUser.getId(), readUser);
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("User loading exit");
        }
        return store;
    }


    public void saveToFile() {
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {

            for (T value : store.values()) {
                oos.writeObject(value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        saveToFile();
    }
}

