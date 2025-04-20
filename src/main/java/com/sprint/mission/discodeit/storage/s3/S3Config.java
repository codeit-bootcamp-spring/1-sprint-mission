package com.sprint.mission.discodeit.storage.s3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

	@Bean
	public AmazonS3Client s3Client(AWSS3Properties properties) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(
			properties.getCredentials().getAccessKey(),
			properties.getCredentials().getSecretKey()
		);

		return (AmazonS3Client)AmazonS3Client.builder()
			.withRegion(Regions.AP_NORTHEAST_2)
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.build();
	}
}