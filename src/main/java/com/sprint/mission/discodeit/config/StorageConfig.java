package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.sse.SseEventSender;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.LocalBinaryContentStorage;
import com.sprint.mission.discodeit.storage.S3BinaryContentStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Slf4j
@Configuration
public class StorageConfig {

    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "S3")
    public S3Client s3Client(
            @Value("${discodeit.storage.s3.access-key}") String accessKey,
            @Value("${discodeit.storage.s3.secret-key}") String secretKey,
            @Value("${discodeit.storage.s3.region}") String region
    ) {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                )).build();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
    public S3Presigner s3Presigner(
            @Value("${discodeit.storage.s3.access-key}") String accessKey,
            @Value("${discodeit.storage.s3.secret-key}") String secretKey,
            @Value("${discodeit.storage.s3.region}") String region
    ) {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                )).build();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
    public BinaryContentStorage s3Storage(
            S3Client s3Client,
            S3Presigner s3Presigner,
            @Value("${discodeit.storage.s3.bucket}") String bucket,
            @Value("${discodeit.storage.s3.presigned-url-expiration:600}") int expirationSeconds,
            ApplicationEventPublisher eventPublisher
    ) {
        log.info("✅ Using S3BinaryContentStorage");
        return new S3BinaryContentStorage(s3Client, s3Presigner, bucket, expirationSeconds,
                eventPublisher);
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local", matchIfMissing = true)
    public BinaryContentStorage localStorage(
            @Value("${discodeit.storage.local.root-path}") String rootPath,
            ApplicationEventPublisher eventPublisher,
            SseEventSender sseEventSender,
            BinaryContentMapper binaryContentMapper,
            BinaryContentRepository binaryContentRepository) {
        log.info("✅ Using LocalBinaryContentStorage");
        return new LocalBinaryContentStorage(rootPath,
                eventPublisher,
                sseEventSender,
                binaryContentMapper,
                binaryContentRepository);
    }
}
