package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileManager {

    // 초기화
    public void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("Directory could not be created. directory : " + directory, e);
            }
        }
    }

    // 저장
    public <T> void save(Path filePath, T data) {
        delete(filePath);
        try(
                FileOutputStream fos = new FileOutputStream(filePath.toFile(), false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("File could not be saved.", e);
        }
    }


    // 조회
    public <T> T load(Path filePath, Class<T> data) {
        if (Files.exists(filePath)) {
            try (
                    FileInputStream fis = new FileInputStream(filePath.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ) {
                Object target = ois.readObject();
                return data.cast(target);
            }  catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("File could not be loaded.", e);
            }
        } else {
            return null;
        }
    }


    public <T> List<T> allLoad(Path filePath) {
        if (Files.exists(filePath)) {
            try {
                List<T> list = Files.list(filePath)
                        .map(path -> {
                            try (
                                    FileInputStream fis = new FileInputStream(path.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis)
                            ) {
                                Object data = ois.readObject();
                                return (T) data;
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException("File could not be loaded.", e);
                            }
                        })
                        .toList();
                return list;
            } catch (IOException e) {
                throw new RuntimeException("Failed to retrieve file list.", e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    // 삭제
    public void delete(Path filePath) {
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("File could not be deleted.", e);
        }
    }
}
