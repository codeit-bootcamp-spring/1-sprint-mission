package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.exception.FileIOException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    public static void createDirectory(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new FileIOException("저장 디렉토리 생성 실패: " + path);
            }
        }
    }
}
