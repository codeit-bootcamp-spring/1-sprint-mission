package com.sprint.mission.discodeit.storage.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "discodeit.storage.s3")
public class AWSS3Properties {

	private S3 s3;
	private Credentials credentials;
	private Region region;
	private int presignedUrlExpiration;

	@Getter
	@Setter
	public static class S3 {
		private String bucket;
	}

	@Getter
	@Setter
	public static class Credentials {
		private String accessKey;
		private String secretKey;
	}

	@Getter
	@Setter
	public static class Region {
		private String staticRegion;
	}

}
