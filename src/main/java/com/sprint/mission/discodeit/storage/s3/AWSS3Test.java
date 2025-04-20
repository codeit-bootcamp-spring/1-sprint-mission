package com.sprint.mission.discodeit.storage.s3;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

public class AWSS3Test {
    private S3Properties s3Properties;


    public static void main(String[] args) throws IOException {
        // Properties 로드
        s3Properties.init(); // 수동 호출

        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                s3Properties.getAccessKey(),
                s3Properties.getSecretKey()
        );

        // S3 Client 생성
        S3Client s3Client = S3Client.builder()
                .region(Region.of(s3Properties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        String bucket = s3Properties.getBucket();
        String key = "test-file-" + UUID.randomUUID();
        String uploadContent = "S3 업로드 테스트입니다.";

        // [1] 업로드
        System.out.println("업로드 중...");
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                RequestBody.fromString(uploadContent));

        System.out.println("업로드 완료: " + key);

        // [2] 다운로드
        System.out.println("다운로드 중...");
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build()
        );

        String downloaded = new String(response.readAllBytes());
        System.out.println("다운로드된 내용: " + downloaded);

        // [3] Presigned URL 생성
        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(s3Properties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(GetObjectRequest.builder()
                                .bucket(bucket)
                                .key(key)
                                .build())
                        .build()
        );

        URL url = presignedRequest.url();
        System.out.println("Presigned URL: " + url);
    }
}
