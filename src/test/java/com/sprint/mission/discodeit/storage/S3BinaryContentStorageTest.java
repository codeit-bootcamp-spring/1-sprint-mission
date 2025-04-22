package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

@Tag("db")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class S3BinaryContentStorageTest {

    private static final Logger log = LoggerFactory.getLogger(S3BinaryContentStorageTest.class);

    @Value("${discodeit.storage.s3.region}")
    private String region;

    @Autowired
    private S3BinaryContentStorage s3BinaryContentStorage;

    @Test
    @DisplayName("S3Client 객체를 세팅한다.")
    void getS3Client() {
        S3Client s3Client;

        // when
        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        // then
        Assertions.assertThat(s3Client).isNotNull();
    }

    @Test
    @DisplayName("MultipartFile을 File 객체로 변환한다.")
    void convertMultipartToFileTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        UUID fileId = UUID.randomUUID();
        String content = "mock-image-data";
        MultipartFile multipartFile = new MockMultipartFile(
                "file", "test.png", "image/png", content.getBytes()
        );

        // reflection - 객체 변환만 해보고 싶어서 사용해 봄
        Method method = S3BinaryContentStorage.class.getDeclaredMethod("convertMultipartFileToFile", MultipartFile.class);
        method.setAccessible(true);

        // when
        File file = (File) method.invoke(s3BinaryContentStorage, multipartFile);


        // then
        Assertions.assertThat(file).isNotNull();
        Assertions.assertThat(file.exists()).isTrue();
        Assertions.assertThat(file.length()).isEqualTo(content.length());

        boolean deleted = file.delete();
        log.info("Temp File Deleted: {}", deleted);
    }

    @Test
    @DisplayName("S3 저장소에 파일을 업로드한다.")
    void putToS3StorageTest() {
        // given
        UUID fileId = UUID.randomUUID();
        String content = "mock-image-data";
        MultipartFile multipartFile = new MockMultipartFile(
                "file", "test.png", "image/png", content.getBytes()
        );
        // when
        UUID result = s3BinaryContentStorage.put(fileId, multipartFile);

        // then
        Assertions.assertThat(result).isEqualTo(fileId);
    }

    @Test
    @DisplayName("PresignedURL이 잘 생성되는지 확인하기 위함")
    void generatePresignedUrlTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        String key = "test/스크린샷 2024-12-29 170654.png ";

        // reflection - 객체 변환만 해보고 싶어서 사용해 봄
        Method method = S3BinaryContentStorage.class.getDeclaredMethod("generatePresignedUrl", String.class, String.class);
        method.setAccessible(true);

        // when
        String url = (String) method.invoke(s3BinaryContentStorage, key, "image/png");

        // then
        Assertions.assertThat(url).isNotNull();
    }

    @Test
    @DisplayName("S3 저장소에 존재하는 파일을 가져온다")
    void getToS3StorageTest() {
        // given
        UUID fileId = UUID.fromString("1d25f4da-9bed-4813-b741-26288d28e7e6");
        // when
        InputStream result = s3BinaryContentStorage.get(fileId);

        // then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("S3에서 해당 PresignedURL을 가져와 해당 데이터를 다운로드 할 수 있도록 한다.")
    void downloadTest() throws IOException {
        // given
        MultipartFile multipartFile = new MockMultipartFile(
                "file", "test.png", "image/png", "mock-image-data2".getBytes()
        );

        BinaryContentResponseDto binaryContentResponseDto = new BinaryContentResponseDto(
                UUID.fromString("1d25f4da-9bed-4813-b741-26288d28e7e6"),
                multipartFile.getOriginalFilename(),
                multipartFile.getSize(),
                multipartFile.getContentType(),
                multipartFile.getBytes()
        );

        // when
        ResponseEntity<?> result = s3BinaryContentStorage.download(binaryContentResponseDto);

        // then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        Assertions.assertThat(result.getHeaders().getLocation()).isNotNull();      // URL 존재
        log.info("Presigned URL → {}", result.getHeaders().getLocation());
    }

}