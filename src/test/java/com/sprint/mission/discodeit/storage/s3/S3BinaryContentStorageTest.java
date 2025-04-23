package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "discodeit.storage.type=s3",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "AWS_S3_REGION=ap-northeast-2",
        "AWS_S3_ACCESS_KEY=dummy",
        "AWS_S3_SECRET_KEY=dummy",
        "AWS_S3_BUCKET=test-bucket"
})
class S3BinaryContentStorageTest {

    @Autowired
    BinaryContentStorage storage;

    @Test
    void download_should_redirect_with_presigned_url() {
        // given
        UUID id = UUID.randomUUID(); // 실제 업로드된 파일이 없어도 presigned URL은 생성됨
        String fileName = "테스트파일.txt"; // 한글 파일명도 테스트
        Long size = 123L;
        String contentType = "text/plain";

        BinaryContentDto dto = new BinaryContentDto(id, fileName, size, contentType);

        // when
        ResponseEntity<?> response = storage.download(dto);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(302); // 리다이렉트
        assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION)).isNotBlank(); // URL 존재
        assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION))
                .contains("filename*="); // 파일명 잘 설정되었는지 확인
    }
}
