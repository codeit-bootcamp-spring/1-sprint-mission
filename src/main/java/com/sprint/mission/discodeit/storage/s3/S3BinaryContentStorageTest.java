package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "discodeit.storage.type=s3",
        "AWS_S3_ACCESS_KEY=AKIA…",
        "AWS_S3_SECRET_KEY=…",
        "AWS_S3_REGION=ap-northeast-2",
        "AWS_S3_BUCKET=discodeit-binary-content-storage-kd"
})
class S3BinaryContentStorageTest {

    @Autowired(required = false)
    private S3BinaryContentStorage storage;

    @Test
    void beanIsLoadedWhenTypeS3() {
        assertThat(storage).isNotNull();
    }

    @Test
    void downloadReturnsRedirect() {
        UUID id = UUID.randomUUID();
        BinaryContentDto dto = new BinaryContentDto(id, "text/plain", "file.txt");

        ResponseEntity<Void> resp = storage.download(dto);

        assertThat(resp.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(resp.getHeaders().getLocation().toString()).startsWith("https://");
    }
}
