package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Service
public class LocalBinaryContentStorage implements BinaryContentStorage{

    private final Path root;

    public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
        this.root = Paths.get(rootPath);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장소 초기화 실패: " + root,  e);
        }
    }

    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }

    @Override
    public UUID put(UUID id, byte[] data) {
        try {
            Path filePath = resolvePath(id);
            Files.write(filePath, data, StandardOpenOption.CREATE_NEW);
            return id;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + id, e);
        }
    }

    @Override
    public InputStream get(UUID id) {
        try {
            Path filePath = resolvePath(id);
            return Files.newInputStream(filePath, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new RuntimeException("파일을 찾을 수 없음: " + id, e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        try{
            Path filePath = resolvePath(binaryContentDto.id());
            File file = filePath.toFile();

            if(!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            InputStream inputStream = new FileInputStream(file);
            Resource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachnment; filename=\"" + binaryContentDto.fileName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, binaryContentDto.contentType())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
