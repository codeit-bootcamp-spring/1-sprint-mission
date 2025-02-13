package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.exception.FileIOException;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class FileManager {

    private final Path path;

    public FileManager(String directoryName) {
        path = Path.of(System.getProperty("user.dir"), directoryName);
        this.createDirectory();
    }

    private void createDirectory() {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new FileIOException("저장 디렉토리 생성 실패: " + path);
            }
        }
    }

    public void createFile(String fileName, byte[] data) {
        Path filePath = path.resolve(fileName);
        try {
            Files.write(filePath, data);
        } catch (IOException e) {
            throw new FileIOException("파일 생성 실패: " + filePath);
        }
    }

    public byte[] readFile(String fileName) {
        Path filePath = path.resolve(fileName);
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new FileIOException("파일 읽기 실패: " + filePath);
        }
    }

    public void deleteFile(String fileName) {
        Path filePath = path.resolve(fileName);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new FileIOException("파일 삭제 실패: " + filePath);
        }
    }
}
