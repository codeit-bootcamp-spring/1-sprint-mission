package com.sprint.mission.storage.s3;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.service.BinaryService;
import com.sprint.mission.service.jcf.serviceImpl.BinaryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.Response;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;

import static com.sprint.mission.common.exception.ErrorCode.*;
import static java.nio.charset.StandardCharsets.*;

@RestController
public class AWSS3Test {
    // 테스트 용 임시 S3 메서드들
    // 업로드 -> 다운로드 순서로 진행해야 KEY 반영

    private final BinaryService binaryService;
    private final S3Client s3Client;
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private String key;

    public AWSS3Test(BinaryService binaryService) {
        settingS3Info();
        this.binaryService = binaryService;
        this.s3Client = getS3Client();
    }

    @PostMapping("upload/s3")
    public void upload(@RequestPart("file-s3") MultipartFile file) throws Exception {
        key = file.getOriginalFilename();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    @GetMapping("download/s3")
    public ResponseEntity<Resource> download(){
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .key(key)
                .bucket(bucket)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        String contentDisposition = ContentDisposition.attachment().filename(key, UTF_8).build().toString();
        String contentType = s3Object.response().contentType();
        Long contentLength = s3Object.response().contentLength();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(contentLength)
                .body(new InputStreamResource(s3Object));
    }

    private void settingS3Info() {
        Properties props = new Properties();
        try(InputStream input = new FileInputStream(".env")) {
            props.load(input);
        } catch (IOException e) {
            throw new CustomException(FILE_CONVERT_ERROR);
        }
        accessKey = props.getProperty("AWS_S3_ACCESS_KEY").trim();
        secretKey = props.getProperty("AWS_S3_SECRET_KEY").trim();
        region = props.getProperty("AWS_S3_REGION").trim();
        bucket = props.getProperty("AWS_S3_BUCKET").trim();
    }

    private S3Client getS3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
