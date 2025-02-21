package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Channel implements Serializable {
  private static final long serialVersionUID = 1L;

  public enum ChannelType {
    PRIVATE, PUBLIC
  }

  private String UUID;
  private ChannelType channelType;
  private String channelName;
  private String description;
  private Instant createdAt;
  private Instant updatedAt;
  private List<String> participatingUsers;


  public static class ChannelBuilder{
    private String UUID = UuidGenerator.generateUUID();
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
  }

  public void updateChannelName(String channelName){
    this.channelName = channelName;
    updateUpdatedAt();
  }



  public void updateUpdatedAt() {
    this.updatedAt = Instant.now();
  }

  @Override
  public String toString() {
    return "Channel{" +
        "UUID='" + UUID + '\'' +
        ", channelType=" + channelType +
        ", channelName='" + channelName + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
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
