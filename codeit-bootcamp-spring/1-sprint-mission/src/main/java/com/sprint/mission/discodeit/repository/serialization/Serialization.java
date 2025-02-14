package com.sprint.mission.discodeit.repository.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

public class Serialization<T> {

    private final Class<T> type;

    public Serialization(Class<T> type) {
        this.type = type;
    }

    public void serialize(Path path, T t) {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(t);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public T deserialize(Path path) {
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return type.cast(ois.readObject());
        } catch (IOException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}
