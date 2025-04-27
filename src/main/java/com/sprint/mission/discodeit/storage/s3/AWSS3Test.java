package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AWSS3Test {

    @Autowired S3Client s3;
    @Autowired S3Presigner presigner;
    @Autowired AWSS3Properties props;

    static final String KEY = "test-folder/sample.txt";
    static final byte[] DATA = "Hello, S3!".getBytes(StandardCharsets.UTF_8);

    @BeforeAll
    static void setup() {
    }

    @AfterAll
    static void cleanup(@Autowired S3Client s3, @Autowired AWSS3Properties props) {
        s3.deleteObject(DeleteObjectRequest.builder()
                .bucket(props.getBucket())
                .key(KEY)
                .build());
    }

    @Test
    void uploadToS3() {
        PutObjectResponse res = s3.putObject(
                PutObjectRequest.builder()
                        .bucket(props.getBucket())
                        .key(KEY)
                        .build(),
                RequestBody.fromBytes(DATA)
        );
        assertThat(res.sdkHttpResponse().isSuccessful()).isTrue();
    }

    @Test
    void downloadFromS3() throws IOException {
        // 먼저 업로드
        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(props.getBucket())
                        .key(KEY)
                        .build(),
                RequestBody.fromBytes(DATA)
        );
        // Download
        ResponseBytes<GetObjectResponse> bytes = s3.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(props.getBucket())
                        .key(KEY)
                        .build()
        );
        byte[] downloaded = bytes.asByteArray();
        assertThat(new String(downloaded, StandardCharsets.UTF_8))
                .isEqualTo("Hello, S3!");
    }

    @Test
    void generatePresignedUrl() {
        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(props.getBucket())
                .key(KEY)
                .build();

        GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getReq)
                .build();

        String url = presigner.presignGetObject(presignReq).url().toString();
        assertThat(url).startsWith("https://");
        System.out.println("Presigned URL: " + url);
    }
}
