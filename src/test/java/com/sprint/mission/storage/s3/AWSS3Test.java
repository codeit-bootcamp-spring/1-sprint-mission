package com.sprint.mission.storage.s3;

import com.sprint.mission.dto.response.BinaryContentDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
public class AWSS3Test {

    private static S3Client s3Client;
    private static String accessKey;
    private static String secretKey;
    private static Region region;
    private static String bucketName;
    private static URI endpoint;
    private static final String contentType = "image/jpeg";
    private static final String KEY = UUID.randomUUID().toString();

    @Container
    private static LocalStackContainer localStack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:latest")
    ).withServices(LocalStackContainer.Service.S3);

    @BeforeAll
    static void setUp() {
        accessKey = localStack.getAccessKey();
        secretKey = localStack.getSecretKey();
        region = Region.of(localStack.getRegion());
        bucketName = "test-bucket-name";
        endpoint = localStack.getEndpointOverride(LocalStackContainer.Service.S3);
        // getEndpointOverride() 메서드를 사용하여 S3 서비스의 엔드포인트를 가져
        s3Client = S3Client.builder()
                .endpointOverride(endpoint)
                .region(region)
                .credentialsProvider(generateCredentialsProvider())
                .forcePathStyle(true)
                .build();
        // 지정한 이름의 S3 버킷을 생성
        s3Client.createBucket(b -> b.bucket(bucketName));
    }



    @DisplayName("S3에 파일 업로드")
    @Test
    void uploadFile() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("운동 테스트 파일", KEY, contentType, "test".getBytes());

        // when
        UUID savedKey = put(UUID.fromString(KEY), mockFile.getBytes());

        //then
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getGetObjectRequest(savedKey.toString(), contentType));
        byte[] uploadedFileBytes = s3Object.readAllBytes();

        // then
        assertThat(uploadedFileBytes).isEqualTo(mockFile.getBytes());
        assertThat(uploadedFileBytes.length).isEqualTo(mockFile.getBytes().length);
    }


    /**
     * 업로드 로직
     */
    private UUID put(UUID key, byte[] bytes) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key.toString())
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
        return key;
    }

    @DisplayName("S3에서 파일 다운로드")
    @Test
    void downLoadFile() throws IOException, InterruptedException {
        // given
        MockMultipartFile mockFile = new MockMultipartFile("다운로드 할 파일", KEY, contentType, "test".getBytes());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .key(KEY)
                .contentType(mockFile.getContentType())
                .bucket(bucketName).build();
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(mockFile.getInputStream(), mockFile.getSize()));
        BinaryContentDto dto = convertMockFileToBinaryDTO(mockFile);

        // when
        String presigned_Request_URL = downLoad(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(presigned_Request_URL))
                .GET().build();
        HttpResponse<byte[]> result = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());

        // then
        assertThat(presigned_Request_URL).isNotNull();
        assertThat(result.statusCode()).isEqualTo(200);
        assertThat(result.body()).isEqualTo(mockFile.getBytes());
    }

    private BinaryContentDto convertMockFileToBinaryDTO(MockMultipartFile mockFile) throws IOException {
        return new BinaryContentDto(UUID.fromString(KEY),
                mockFile.getOriginalFilename(),
                mockFile.getSize(),
                mockFile.getContentType(),
                mockFile.getBytes()
        );
    }

    /**
     * 다운로드 로직
     */
    private String downLoad(BinaryContentDto content) {
        return generatePresignedUrl(content.id().toString(), content.contentType());
    }

    private String generatePresignedUrl(String key, String contentType) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .key(key)
                .responseContentType(contentType)
                .bucket(bucketName)
                .build();

        GetObjectPresignRequest getPresignedRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(3))
                .getObjectRequest(getObjectRequest)
                .build();

        S3Presigner s3Presigner = generateS3Presigner();
        PresignedGetObjectRequest s3PresignedRequest = s3Presigner.presignGetObject(getPresignedRequest);
        return s3PresignedRequest.url().toString();
    }

    /**
     * 편의
     */
    private GetObjectRequest getGetObjectRequest(String key, String contentType) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .responseContentType(contentType)
                .build();
    }

    private static S3Presigner generateS3Presigner(){
        return S3Presigner.builder()
                .region(region)
                .credentialsProvider(generateCredentialsProvider())
                .endpointOverride(endpoint)
                .build();
    }

    private static StaticCredentialsProvider generateCredentialsProvider() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey,secretKey);
        return StaticCredentialsProvider.create(credentials);
    }
}
