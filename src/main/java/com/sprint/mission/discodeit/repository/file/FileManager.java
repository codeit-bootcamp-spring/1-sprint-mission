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
        try {
            Files.write(path, data);
        } catch (IOException e) {
            throw new FileIOException("파일 생성 실패: " + path);
        }
    }

    public byte[] readFile(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new FileIOException("파일 읽기 실패: " + path);
        }
    }

    public void deleteFile(Path path) {
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("파일 삭제 실패: " + path);
            }
        }
    }
}
