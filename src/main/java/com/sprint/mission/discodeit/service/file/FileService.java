package com.sprint.mission.discodeit.service.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class FileService {

    private final Path baseDirectory = Paths.get(System.getProperty("user.dir")).resolve("data");

    public Path getBaseDirectory() {
        return baseDirectory;
    }

    public static <T> List<T> load(Path directory) {
        if (Files.exists(directory)) {
            try {
                List<T> list = Files.list(directory)
                        .map(path -> {
                            try (
                                    FileInputStream fis = new FileInputStream(path.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis)
                            ) {
                                Object data = ois.readObject();
                                return (T) data;
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList();
                return list;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    public static void init(Path directory) {
        // 저장할 경로의 파일 초기화
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T> void save(Path filePath, T data) {
        try(
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(Path filePath){
        try{
            Files.deleteIfExists(filePath);
            System.out.println("delete success");
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteDataDirectory() {
        Path dataDir = Paths.get(System.getProperty("user.dir")).resolve("data");
        try {
            if (Files.exists(dataDir)) {
                Files.walk(dataDir)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.err.println("Failed to delete: " + path);
                            }
                        });
                System.out.println("Data directory deleted successfully");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete data directory", e);
        }
    }

}
