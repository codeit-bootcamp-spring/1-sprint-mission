package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.ChannelValidationException;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Getter
@Setter
public class Channel implements Serializable {
  private static final long serialVersionUID = 1L;

  public enum ChannelType {
    CHAT, VOICE
  }

  private final String UUID;
  private final String serverUUID;
  private final ChannelType channelType;
  private String channelName;
  private int maxNumberOfPeople;
  private Instant createdAt;
  private Instant updatedAt;
  private Boolean isPrivate;
  private List<String> participatingUsers;

  private Channel(ChannelBuilder builder) {
    this.UUID = java.util.UUID.randomUUID().toString();
    this.serverUUID = builder.serverUUID;
    this.channelType = builder.channelType;
    this.channelName = builder.channelName;
    this.maxNumberOfPeople = builder.maxNumberOfPeople;
    this.isPrivate = builder.isPrivate;
    this.participatingUsers = builder.participatingUsers == null ? Collections.emptyList() : builder.participatingUsers;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public static class ChannelBuilder {
    private String serverUUID;
    private String channelName;
    private ChannelType channelType;
    private int maxNumberOfPeople = 50;
    private Boolean isPrivate = false;
    private List<String> participatingUsers;

    public ChannelBuilder(String channelName, ChannelType channelType) {
      if (channelName == null || channelName.isEmpty()) {
        this.channelName = "";
      }
      this.channelName = channelName;
      this.channelType = channelType;
    }

    public ChannelBuilder serverUUID(String serverUUID) {
      this.serverUUID = serverUUID;
      return this;
    }

    public ChannelBuilder maxNumberOfPeople(int maxNumberOfPeople) {
      this.maxNumberOfPeople = maxNumberOfPeople;
      return this;
    }

    public ChannelBuilder isPrivate(Boolean isPrivate) {
      this.isPrivate = isPrivate;
      return this;
    }

    public ChannelBuilder participatingUsers(List<String> userIds){
      if(!this.isPrivate) throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE);
      this.participatingUsers = userIds;
      return this;
    }

    public Channel build() {
      return new Channel(this);
    }
  }

  public void updatePrivate(Boolean isPrivate) {
    this.isPrivate = isPrivate;
    this.updatedAt = Instant.now();
  }

  public void updateUpdatedAt(){
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
