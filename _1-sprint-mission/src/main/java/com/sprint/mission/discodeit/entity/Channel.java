package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel extends Common implements Serializable {

  private static final long serialVersionUID = 1L; //직렬화 버전

  //필드
  private ChannelType channelType;
  private String channelName; // 채널 이름
  private String description; // 채널 설명

  public Channel(ChannelType channelType, String channelName, String description) {
    super(UUID.randomUUID(), Instant.now());
    //검증
    if (channelType == null) {
      throw new IllegalArgumentException("채널 타입은 필수입니다.(PUBLIC,PRIVATE)");
    }
    if (channelName == null || channelName.trim().isEmpty() || channelName.length() > 30) {
      throw new IllegalArgumentException("채널 이름은 1~30자 사이여야 합니다.");
    }
    if (description != null && description.length() > 100) {
      throw new IllegalArgumentException("채널 설명은 최대 100자까지 가능합니다.");
    }
    this.channelType = channelType;
    this.channelName = channelName;
    this.description = description;
  }

  // update 메소드
  public void updateChannel(String newChannelName, String newDescription) {
    if (newChannelName != null && !newChannelName.trim().isEmpty() && !newChannelName.equals(
        this.channelName)) {
      this.channelName = newChannelName;
      updateTimestamp();
    }
    if (newDescription != null && !newDescription.trim().isEmpty() && !newDescription.equals(
        this.description)) {
      this.description = newDescription;
      updateTimestamp();
    }
  }

}
