package com.sprint.mission.discodeit.s3;




import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import com.sprint.mission.discodeit.storage.s3.AWSS3Properties;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {
	private S3Client mockS3Client;
	private S3Presigner mockS3Presigner;
	private AWSS3Properties awss3Properties;

	@BeforeEach
	void setUp() {
		mockS3Client = mock(S3Client.class);
		mockS3Presigner = mock(S3Presigner.class);

		awss3Properties = new AWSS3Properties();

		AWSS3Properties.Credentials credentials = new AWSS3Properties.Credentials();
		credentials.setAccessKey("mock-access-key");
		credentials.setSecretKey("mock-secret-key");

		AWSS3Properties.Region region = new AWSS3Properties.Region();
		region.setStaticRegion("ap-northeast-2");

		AWSS3Properties.S3 s3 = new AWSS3Properties.S3();
		s3.setBucket("mock-bucket");

		awss3Properties.setCredentials(credentials);
		awss3Properties.setRegion(region);
		awss3Properties.setS3(s3);
	}

	@Test
	@DisplayName("S3 Presigned URL 생성 테스트 ")
	void presignedUrlTest() throws Exception {
		//given
		String key = "test-presigned/sample.txt";

		PresignedGetObjectRequest mockPresigned = mock(PresignedGetObjectRequest.class);
		when(mockPresigned.url()).thenReturn(new URL("https://mock-url.com"));
		when(mockS3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(mockPresigned);


		GetObjectRequest getRequest = GetObjectRequest.builder()
			.bucket(awss3Properties.getS3().getBucket())
			.key(key)
			.build();

		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
			.signatureDuration(Duration.ofMinutes(10))
			.getObjectRequest(getRequest)
			.build();

		//when
		URL resultUrl = mockS3Presigner.presignGetObject(presignRequest).url();

		//then
		assertThat(resultUrl.toString()).isEqualTo("https://mock-url.com");
	}

	@Test
	@DisplayName("S3 업로드 테스트")
	void uploadTest() {
		//given
		String key = "test-upload/" + UUID.randomUUID();
		String content = "This is a test upload";

		PutObjectResponse mockResponse = mock(PutObjectResponse.class);
		when(mockResponse.eTag()).thenReturn("mock-etag");

		when(mockS3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(mockResponse);

		PutObjectRequest putRequest = PutObjectRequest.builder()
			.bucket(awss3Properties.getS3().getBucket())
			.key(key)
			.build();

		//when
		PutObjectResponse response = mockS3Client.putObject(
			putRequest,
			RequestBody.fromInputStream(new ByteArrayInputStream(content.getBytes()), content.length())
		);

		//then
		assertThat(response).isNotNull();
		assertThat(response.eTag()).isEqualTo("mock-etag");
	}

	@Test
	@DisplayName("S3 다운로드 테스트")
	void downloadTest() {
		//given
		String key = "test-download/sample.txt";

		ResponseBytes<GetObjectResponse> mockBytes = mock(ResponseBytes.class);
		when(mockBytes.asUtf8String()).thenReturn("mock-content");

		when(mockS3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(mockBytes);

		GetObjectRequest getRequest = GetObjectRequest.builder()
			.bucket(awss3Properties.getS3().getBucket())
			.key(key)
			.build();

		//when
		ResponseBytes<GetObjectResponse> response = mockS3Client.getObjectAsBytes(getRequest);

		//then
		assertThat(response.asUtf8String()).isEqualTo("mock-content");
	}

}
