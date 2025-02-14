package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.exception.FileIOException;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Component
public class FileManager {

    public void createDirectory(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new FileIOException("저장 디렉토리 생성 실패: " + path);
            }
        }
    }

    public void createFile(Path path, byte[] data) {
        Path filePath = path.resolve(path);
        try {
            Files.write(filePath, data);
        } catch (IOException e) {
            throw new FileIOException("파일 생성 실패: " + filePath);
        }
    }

    public byte[] readFile(Path path) {
        Path filePath = path.resolve(path);
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new FileIOException("파일 읽기 실패: " + filePath);
        }
    }

    public void deleteFile(Path path) {
        Path filePath = path.resolve(path);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new FileIOException("파일 삭제 실패: " + filePath);
        }
    }
}
