package com.sprint.mission.discodeit.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@SpringBootTest
@ActiveProfiles("test")
class S3BinaryContentStorageTest {

    @Autowired
    S3BinaryContentStorage s3BinaryContentStorage;

    // TODO: localstack 사용해보기
    @DisplayName("S3에 파일 업로드 성공하기")
    @Test
    void put() {
        // given
        UUID uuid = UUID.randomUUID();
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(new File("binaryContents/file.txt").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // when
        UUID saved = s3BinaryContentStorage.put(uuid, bytes);

        // then
        assertThat(saved).isEqualTo(uuid);

    }

    @Test
    void get() {
    }

    @Test
    void download() {
    }
}