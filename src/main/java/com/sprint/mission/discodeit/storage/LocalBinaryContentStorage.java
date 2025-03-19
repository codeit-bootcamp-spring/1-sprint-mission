package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
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
            throw new CustomException(ErrorCode.FAILED_TO_CREATE_DIRECTORY);
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
            throw new CustomException(ErrorCode.FAILED_TO_SAVE_DATA);
        }
    }

    @Override
    public InputStream get(UUID fileId) {
        try {
            // UUID로 시작하는 파일을 검색
            Path filePath = Files.list(root)
                    .filter(path -> path.getFileName().toString().startsWith(fileId.toString()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

            return Files.newInputStream(filePath, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_TO_LOAD_DATA);
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
            // ✅ 파일 존재 여부 확인
            if (!Files.exists(filePath)) {
                throw new CustomException(ErrorCode.FILE_NOT_FOUND);
            }

            // ✅ 파일명 UTF-8 인코딩 (깨짐 방지)
            String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8)
                    .replaceAll(REPLACE_REGEX, REPLACE_VALUE); // 공백을 `%20`으로 변환

            byte[] fileData = Files.readAllBytes(filePath);
            ByteArrayResource resource = new ByteArrayResource(fileData);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, HEADER_VALUE + encodedFileName)
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file: " + filePath, e);
        }
    }
}
