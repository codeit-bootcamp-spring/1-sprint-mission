package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.InvalidOperationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Getter
@Setter
public class Channel implements Serializable {
  private static final long serialVersionUID = 1L;

  public enum ChannelType {
    CHAT, VOICE
  }

  private String UUID;
  private String serverUUID;
  private ChannelType channelType;
  private String channelName;
  private int maxNumberOfPeople;
  private Instant createdAt;
  private Instant updatedAt;
  private Boolean isPrivate;
  private List<String> participatingUsers;

  public Channel(
      String serverUUID,
      ChannelType channelType,
      String channelName,
      int maxNumberOfPeople,
      Boolean isPrivate,
      List<String> participatingUsers) {
    this.UUID = java.util.UUID.randomUUID().toString();
    this.serverUUID = serverUUID;
    this.channelType = channelType;
    this.channelName = channelName;
    this.maxNumberOfPeople = maxNumberOfPeople;
    this.isPrivate = isPrivate;
    this.participatingUsers = (participatingUsers == null) ? new ArrayList<>() : participatingUsers;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public void updatePrivate(Boolean isPrivate) {
    this.isPrivate = isPrivate;
    this.updatedAt = Instant.now();
  }

  public void updateUpdatedAt() {
    this.updatedAt = Instant.now();
  }

  @Override
  public String toString() {
    return "Channel{" +
        "UUID='" + UUID + '\'' +
        ", serverUUID='" + serverUUID + '\'' +
        ", channelType=" + channelType +
        ", channelName='" + channelName + '\'' +
        ", maxNumberOfPeople=" + maxNumberOfPeople +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", isPrivate=" + isPrivate +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Channel channel = (Channel) o;
    return Objects.equals(UUID, channel.UUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(UUID);
  }
}
