package com.sprint.mission.discodeit.repository.file;


import com.sprint.mission.discodeit.global.error.ErrorCode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStorage {

    private final Path baseDirectory = Paths.get(System.getProperty("user.dir"), "data");

    public void init(Path directory) {
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.DIRECTORY_INIT_FAIL.format("path: " + directory));
        }
    }

    public void clearDataDirectory() {
        try {
            if (Files.exists(baseDirectory)) {
                Files.walk(baseDirectory)
                        .sorted((a, b) -> b.compareTo(a)) // 파일부터 삭제 후 디렉토리 삭제
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                throw new RuntimeException(ErrorCode.FILE_REMOVE_FAIL
                                        .format("path: " + path.toString()));
                            }
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.FILES_LOAD_FAIL.format("path: " + baseDirectory));
        }
    }


    public <T> T save(Path filePath, T data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.FILE_WRITE_FAIL
                    .format("path: " + filePath + "data: " + data));
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
                        throw new RuntimeException(ErrorCode.FILE_READ_FAIL.format("path: " + file.toString()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.FILES_LOAD_FAIL.format("path: " + directory));
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
                        throw new RuntimeException(ErrorCode.FILE_REMOVE_FAIL
                                .format("path: " + file.toString()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.FILES_LOAD_FAIL.format("path: " + directory));
        }
    }

    public Path getFilePath(Path directory, UUID id) {
        return directory.resolve(id.toString().concat(".ser"));
    }

    public Path getDirectory(String subDir) {
        return baseDirectory.resolve(subDir);
    }

    private <T> T deserialize(Path filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (T) ois.readObject();
        }
    }

}
