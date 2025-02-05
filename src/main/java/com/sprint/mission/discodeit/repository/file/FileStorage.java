package com.sprint.mission.discodeit.repository.file;


import com.sprint.mission.discodeit.common.ErrorMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {

    public void init(Path directory) {
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.DIRECTORY_INIT_FAIL.format(directory));
        }
    }

    public <T> T save(Path filePath, T data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FILE_WRITE_FAIL.format(filePath, data));
        }
    }

    public <T> List<T> load(Path directory) {
        List<T> list = new ArrayList<>();
        try {
            if (!Files.exists(directory)) {
                return list;
            }

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                for (Path file : stream) {
                    try {
                        T data = deserialize(file);
                        list.add(data);
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(ErrorMessage.FILE_READ_FAIL.format(file.toString()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FILES_LOAD_FAIL.format(directory));
        }
        return list;
    }

    public void remove(Path directory, Object object) {
        try {
            if (!Files.exists(directory)) {
                return;
            }

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                for (Path file : stream) {
                    try {
                        Object data = deserialize(file);
                        if (data.equals(object)) {
                            Files.deleteIfExists(file);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(ErrorMessage.FILE_REMOVE_FAIL.format(file.toString()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FILES_LOAD_FAIL.format(directory));
        }
    }

    private <T> T deserialize(Path filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (T) ois.readObject();
        }
    }

}
