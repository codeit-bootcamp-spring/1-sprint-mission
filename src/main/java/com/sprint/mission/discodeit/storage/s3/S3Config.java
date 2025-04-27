package com.sprint.mission.discodeit.storage.s3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client(AWSS3Properties props) { //자동 주입을 할 수 없습니다. 'AWSS3Properties' 타입의 bean이 두 개 이상 있습니다.
        return S3Client.builder()
                .region(Region.of(props.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        props.getAccessKey(),
                                        props.getSecretKey())))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner(AWSS3Properties props) { // 자동 주입을 할 수 없습니다. 'AWSS3Properties' 타입의 bean이 두 개 이상 있습니다.
        return S3Presigner.builder()
                .region(Region.of(props.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        props.getAccessKey(),
                                        props.getSecretKey())))
                .build();
    }
}
