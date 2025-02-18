package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
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


  public static class ChannelBuilder{
    private String UUID = UuidGenerator.generateUUID();
    private String serverUUID = "none";
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
  }

  public void updateChannelName(String channelName){
    this.channelName = channelName;
    updateUpdatedAt();
  }

  public void updateMaxNumberOfPeople(int maxNumberOfPeople){
    this.maxNumberOfPeople = maxNumberOfPeople;
    updateUpdatedAt();
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
