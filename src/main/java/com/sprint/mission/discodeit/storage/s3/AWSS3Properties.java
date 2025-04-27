// src/main/java/com/sprint/mission/discodeit/storage/s3/AWSS3Properties.java
package com.sprint.mission.discodeit.storage.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discodeit.storage.s3")
public class AWSS3Properties {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private long presignedUrlExpiration;  // 초 단위

    // getters & setters
    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }
    public long getPresignedUrlExpiration() { return presignedUrlExpiration; }
    public void setPresignedUrlExpiration(long presignedUrlExpiration) {
        this.presignedUrlExpiration = presignedUrlExpiration;
    }
}
