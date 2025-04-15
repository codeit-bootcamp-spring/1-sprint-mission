package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.file.FileNotFoundCustomException;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class S3BinaryContentStorageTest {

    private BinaryContentStorage storage;
    private String bucket;

    @BeforeEach
    void setUp() throws Exception {
        // .env가 없으면 테스트 건너뜀
        if (!Files.exists(Paths.get(".env"))) {
            Assumptions.assumeTrue(false, ".env 파일이 없어서 테스트를 건너뜁니다.");
        }

        // .env 로드
        Properties props = new Properties();
        props.load(Files.newBufferedReader(Paths.get(".env")));

        String accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        String secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        String region = props.getProperty("AWS_S3_REGION");
        bucket = props.getProperty("AWS_S3_BUCKET");

        // Presigned URL 만료 시간은 생성자에서 .env에서 직접 읽음
        storage = new S3BinaryContentStorage(accessKey, secretKey, region, bucket);
    }

    @DisplayName("s3에 데이터를 업로드 가능하다.")
    @Test
    void putAndGet_shouldUploadAndDownload() throws Exception {
        UUID id = UUID.randomUUID();
        byte[] data = "Hello, Binary Content".getBytes();

        UUID savedId = storage.put(id, data);
        InputStream downloaded = storage.get(savedId);
        String result = new String(downloaded.readAllBytes());

        assertThat(savedId).isEqualTo(id);
        assertThat(result).isEqualTo("Hello, Binary Content");
    }

    @DisplayName("해당하는 키가 없다면 s3에서 객체 조회에 실패한다.")
    @Test
    void get_shouldThrow_whenFileNotExist() {
        UUID nonexistentId = UUID.randomUUID();

        assertThatThrownBy(() -> storage.get(nonexistentId))
                .isInstanceOf(FileNotFoundCustomException.class);
    }

    @DisplayName("s3에서 파일을 다운로드 할 수 있다.")
    @Test
    void download_shouldReturnPresignedUrl() throws Exception {
        // 파일 업로드
        UUID id = UUID.randomUUID();
        byte[] data = "File for presigned URL test".getBytes();
        storage.put(id, data);

        // DTO 생성 후 다운로드 요청
        BinaryContentDto dto = new BinaryContentDto(id, "file.txt", data.length, "text/plain");
        ResponseEntity<?> response = storage.download(dto);

        assertThat(response.getStatusCode().value()).isEqualTo(302);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }
}