package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.exception.data.DataSaveFailedException;
import com.sprint.mission.discodeit.exception.storage.CreateFailedLocalDirectoryException;
import com.sprint.mission.discodeit.exception.storage.FileNotFoundInLocalDirectoryException;
import com.sprint.mission.discodeit.exception.storage.LoadFailedLocalDirectoryException;
import com.sprint.mission.discodeit.exception.storage.SaveFailedLocalDirectoryException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;

    public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
        this.root = Paths.get(System.getProperty("user.dir")).resolve(rootPath); // 프로젝트 루트 기준으로 저장
    }

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            log.error("파일 저장 디렉토리 생성 실패 - path: {}, message: {}", root, e.getMessage(), e);
            throw new CreateFailedLocalDirectoryException(root);
        }
    }

    private Path resolvePath(String savedFilename) {
        return root.resolve(savedFilename);
    }

    @Override
    public UUID put(UUID fileId, MultipartFile file) {
        try {
            String SEPERATE_VALUE = ".";
            int dotIndex = file.getOriginalFilename().lastIndexOf(SEPERATE_VALUE);
            String extension = file.getOriginalFilename().substring(dotIndex);

            Path filePath = resolvePath(fileId.toString() + extension);

            Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE_NEW);
            return fileId;
        } catch (IOException e) {
            log.error("파일 저장 시 오류 발생 - id: {}, messasge: {}", fileId, e.getMessage(), e);
            throw new SaveFailedLocalDirectoryException(fileId);
        }
    }

    @Override
    public InputStream get(UUID fileId) {
        log.debug("파일 조회 요청 - id: {}", fileId);
        try {
            // UUID로 시작하는 파일을 검색
            Path filePath = Files.list(root)
                    .filter(path -> path.getFileName().toString().startsWith(fileId.toString()))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.warn("파일을 찾을 수 없습니다. - id: {}", fileId);
                        throw new LoadFailedLocalDirectoryException(fileId);
                    });

            return Files.newInputStream(filePath, StandardOpenOption.READ);
        } catch (IOException e) {
            log.error("파일 로드 시 오류가 발생 - id: {}, message: {}", fileId, e.getMessage(), e);
            throw new LoadFailedLocalDirectoryException(fileId);
        }
    }

    @Override
    public ResponseEntity<ByteArrayResource> download(BinaryContentResponseDto binaryContentResponseDto) {
        UUID fileId = binaryContentResponseDto.id();
        String originalFileName = binaryContentResponseDto.fileName();

        String REPLACE_VALUE = "%20";
        String REPLACE_REGEX = "\\+";
        String HEADER_VALUE = "attachment; filename*=UTF-8''";

        // 확장자가 포함된 파일명 설정
        int dotIndex = originalFileName.lastIndexOf(".");
        String extension = (dotIndex != -1) ? originalFileName.substring(dotIndex) : "";
        Path filePath = resolvePath(fileId.toString() + extension);

        try {
            // 파일 존재 여부 확인
            if (!Files.exists(filePath)) {
                log.warn("해당 Path에 대상 파일이 존재하지 않습니다. - Path: {}", filePath);
                throw new FileNotFoundInLocalDirectoryException(filePath);
            }

            // 파일명 UTF-8 인코딩 (깨짐 방지)
            String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8)
                    .replaceAll(REPLACE_REGEX, REPLACE_VALUE); // 공백을 `%20`으로 변환

            byte[] fileData = Files.readAllBytes(filePath);
            ByteArrayResource resource = new ByteArrayResource(fileData);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, HEADER_VALUE + encodedFileName)
                    .body(resource);
        } catch (IOException e) {
            log.error("파일 다운로드 데이터 전달 시 오류 발생 - id: {}, path: {}, message: {}", binaryContentResponseDto.id(), filePath, e.getMessage(), e);
            throw new DataSaveFailedException("FileStorage", binaryContentResponseDto.id(), e);
        }
    }
}
