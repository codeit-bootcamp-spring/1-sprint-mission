package com.sprint.mission.discodeit.storage;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.s3.AWSS3Properties;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {
	private final String accessKey;
	private final String secretKey;
	private final String region;
	private final String bucket;

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

	public S3BinaryContentStorage(AWSS3Properties awsS3Properties) {
		this.accessKey = awsS3Properties.getCredentials().getAccessKey();
		this.secretKey = awsS3Properties.getCredentials().getSecretKey();
		this.region = awsS3Properties.getRegion().getStaticRegion();
		this.bucket = awsS3Properties.getS3().getBucket();

		AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
		URI endpoint = URI.create("https://s3." + region + ".amazonaws.com");

		S3Configuration s3Config = S3Configuration.builder()
			.pathStyleAccessEnabled(true)
			.build();

		this.s3Client = S3Client.builder()
			.region(Region.of(region))
			.credentialsProvider(StaticCredentialsProvider.create(credentials))
			.endpointOverride(endpoint)
			.serviceConfiguration(s3Config)
			.build();

		this.s3Presigner = S3Presigner.builder()
			.region(Region.of(region))
			.credentialsProvider(StaticCredentialsProvider.create(credentials))
			.endpointOverride(endpoint)
			.build();
	}

	@Override
	public UUID put(UUID id, byte[] data) {
		String key = id.toString();

		PutObjectRequest request = PutObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.build();

		s3Client.putObject(request, RequestBody.fromBytes(data));
		return id;
	}

	@Override
	public InputStream get(UUID id) {
		GetObjectRequest request = GetObjectRequest.builder()
			.bucket(bucket)
			.key(id.toString())
			.build();

		return s3Client.getObject(request);

	}

	@Override
	public ResponseEntity<?> download(BinaryContentDto metaData) {
		try {
			URL url = s3Presigner.presignGetObject(
				GetObjectPresignRequest.builder()
					.signatureDuration(Duration.ofMinutes(5))
					.getObjectRequest(GetObjectRequest.builder()
						.bucket(bucket)
						.key(metaData.id().toString())
						.build())
					.build()
			).url();

			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(url.toURI());
			return new ResponseEntity<>(headers, HttpStatus.FOUND);
		}catch (URISyntaxException e){
			throw new RuntimeException("URL to URI 변환 실패 : ", e);
		}
	}

	public String generatePresignedUrl(String key, String contentType) {
		GetObjectRequest getRequest = GetObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.build();

		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
			.signatureDuration(Duration.ofMinutes(5))
			.getObjectRequest(getRequest)
			.build();

		PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignRequest);
		return presigned.url().toString();
	}
}
