package com.sprint.mission.discodeit.s3;

import static org.mockito.Mockito.*;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sprint.mission.discodeit.storage.S3BinaryContentStorage;
import com.sprint.mission.discodeit.storage.s3.AWSS3Properties;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

public class S3BinaryContentStorageTest {

	private S3Client s3Client;
	private S3Presigner s3Presigner;
	private AWSS3Properties awsS3Properties;


	@BeforeEach
	void setUp() {
		s3Client = mock(S3Client.class);
		s3Presigner = mock(S3Presigner.class);

		AWSS3Properties.Credentials credentials = new AWSS3Properties.Credentials();
		credentials.setAccessKey("test-access-key");
		credentials.setSecretKey("test-secret-key");

		AWSS3Properties.Region region = new AWSS3Properties.Region();
		region.setStaticRegion("ap-northeast-2");

		AWSS3Properties.S3 s3 = new AWSS3Properties.S3();
		s3.setBucket("test-bucket");

		awsS3Properties = new AWSS3Properties();
		awsS3Properties.setCredentials(credentials);
		awsS3Properties.setRegion(region);
		awsS3Properties.setS3(s3);
	}

	@Test
	@DisplayName("파일 업로드 테스트")
	void testPut() {
		//given
		UUID id = UUID.randomUUID();
		byte[] content = "test".getBytes();

		PutObjectResponse mockResponse = mock(PutObjectResponse.class);
		when(mockResponse.eTag()).thenReturn(UUID.randomUUID().toString());

		when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(mockResponse);

		S3BinaryContentStorage storage = new S3BinaryContentStorage(awsS3Properties);

		//when
		UUID result = storage.put(id, content);

		//then
		Assertions.assertThat(result).isEqualTo(id);
		verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
	}


}



