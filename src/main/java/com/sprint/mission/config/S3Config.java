//package com.sprint.mission.config;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
//@Configuration
//@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
//public class S3Config {
//
//    private String accessKey;
//    private String secretKey;
//    private String region;
//
//    @Bean
//    public S3Client s3Client() {
//        setProperties();
//        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
//        return S3Client.builder()
//                .region(Region.of(region))
//                .credentialsProvider(StaticCredentialsProvider.create(credentials))
//                .build();
//    }
//
//    private void setProperties() {
//        Properties props = new Properties();
//
//        try(InputStream object = new FileInputStream(".env")) {
//            props.load(object);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
//        secretKey = props.getProperty("AWS_S3_SECRET_KEY");
//        region = props.getProperty("AWS_S3_REGION");
//    }
//}
