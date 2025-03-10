package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage{

    @Value("${discodeit.storage.local.root-path}")
    private String rootPath; // 설정 파일에서 주입받는 루트 경로

    private Path root; // 로컬 디스크의 루트 경로

    @PostConstruct
    public void init() throws IOException {
        try {
            this.root = Paths.get(rootPath);
            if(!Files.exists(root)) {
                Files.createDirectories(root);
                log.info("디렉토리 생성 : " + root);
            } else {
                log.info("디렉토리 이미 존재 : " + root);
            }
        } catch (IOException e) {
            log.error("저장소 초기화 실패: {}", e.getMessage());
            throw new IOException("저장소 초기화 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public UUID put(UUID id, byte[] data) throws IOException {
        try {
            Path filePath = resolvePath(id);
            Files.write(filePath, data);
            log.info("파일 저장 완료 : " + filePath);
            return id;
        } catch (IOException e) {
            log.error("파일 저장 실패: {}", e.getMessage());
            throw new IOException("파일 저장 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream get(UUID id) throws IOException {
        Path filePath = resolvePath(id);
        if(Files.exists(filePath)) {
            try {
                return new ByteArrayInputStream(Files.readAllBytes(filePath));
            } catch (IOException e) {
                log.error("파일 읽기 실패: {}", e.getMessage());
                throw new IOException("파일 읽기 실패: " + e.getMessage(), e);
            }
        } else {
            log.error("파일을 찾을 수 없습니다: {}", filePath);
            throw new ResourceNotFoundException("파일", "id", id);
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto binaryContentDto) throws IOException {
        try {
            UUID uuid = binaryContentDto.getId(); // DTO에서 UUID 가져옴
            Path filePath = resolvePath(uuid);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + uuid + "\"")
                        .body(resource);
            } else {
                log.error("다운로드할 파일을 찾을 수 없습니다: {}", filePath);
                throw new ResourceNotFoundException("파일", "id", uuid);
            }
        } catch (IOException e) {
            log.error("파일 다운로드 실패: {}", e.getMessage());
            throw new IOException("파일 다운로드 실패: " + e.getMessage(), e);
        }
    }

    private Path resolvePath(UUID id) {
       return root.resolve(id.toString());
    }
}
