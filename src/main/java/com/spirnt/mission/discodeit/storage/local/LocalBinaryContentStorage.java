package com.spirnt.mission.discodeit.storage.local;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.exception.BinaryContent.FileException;
import com.spirnt.mission.discodeit.storage.BinaryContentStorage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;

    public LocalBinaryContentStorage(
        @Value("${discodeit.storage.local.root-path}") Path root
    ) {
        this.root = root;
    }

//    // Bean 생성 시 자동 호출하여 루트 디렉토리 초기화
//    @PostConstruct
//    void init() {
//        File[] files = root.toFile().listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isFile()) {
//                    file.delete();
//                }
//            }
//        }
//    }

    /// 파일 실제 저장 위치 정의
    Path resolvePath(UUID binaryContentId) {
        return Paths.get(root + "/" + binaryContentId);
    }

    // 파일을 로컬에 저장
    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        Path path = resolvePath(binaryContentId);
        File file = new File(String.valueOf(path));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        } catch (IOException e) {
            log.warn("[스토리지에 파일 저장 중 오류가 발생했습니다.]");
            throw new FileException(Map.of("binaryContentId", binaryContentId));
        }
        return binaryContentId;
    }

    // 파일을 InputStream 데이터 타입으로 반환
    @Override
    public InputStream get(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        try {
            return new FileInputStream(String.valueOf(path));
        } catch (FileNotFoundException e) {
            log.warn("[스토리지에 파일이 없습니다.]");
            throw new FileException(Map.of("binaryContentId", binaryContentId));
        }
    }

    // 파일 다운로드
    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        Path path = resolvePath(binaryContentDto.getId());
        Resource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, binaryContentDto.getContentType())
            .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment() // (6)
                .filename(binaryContentDto.getFileName(), StandardCharsets.UTF_8)
                .build()
                .toString())
            .body(resource);
    }
}
