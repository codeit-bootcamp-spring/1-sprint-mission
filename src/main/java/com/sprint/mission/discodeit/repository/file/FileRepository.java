package com.sprint.mission.discodeit.repository.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public abstract class FileRepository<T extends Serializable> {
    private final String EXTENSION = ".ser";
    private Path DIRECTORY;

    protected FileRepository(String fileDirectory) {
        this.DIRECTORY = Path.of(fileDirectory);
    }
    protected Path getDIRECTORY() {
        return DIRECTORY;
    }
    protected Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }
    protected void saveToFile(Path path, T data) {
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos);){
            oos.writeObject(data);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected Optional<T> loadFromFile(Path path) {
        if(!Files.exists(path)) {
            return Optional.empty();
        }
        try (FileInputStream fis = new FileInputStream(path.toFile());
        ObjectInputStream ois = new ObjectInputStream(fis)) {
            return Optional.ofNullable((T) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    protected void deleteFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.println("파일 삭제 중 오류가 발생했습니다." + e.getMessage());
        }
    }
}
