package com.spirnt.mission.discodeit.repository.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

// Serializable한 객체만 T로 들어올 수 있음
public abstract class FileRepository<T extends Serializable> {
    private final String extension = ".ser";
    private Path directory;   // ser 파일 저장할 경로

    protected FileRepository(String fileDirectory) {
        this.directory = Path.of(fileDirectory);
    }

    protected Path getDirectory() {
        return directory;
    }

    protected Path resolvePath(UUID id) {
        return directory.resolve(id + extension);
    }

    protected void saveToFile(Path path, T data) {


        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Optional<T> loadFromFile(Path path) {
        if (!Files.exists(path)) {
            return Optional.empty(); // 파일이 없으면 Optional.empty() 반환
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
            // 파일이 존재하면 삭제
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.println("파일 삭제 중 오류 발생: " + e.getMessage());
        }
    }

}
