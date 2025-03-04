package com.spirnt.mission.discodeit.dto.channel;

import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ChannelDto {

  private UUID id;
  private String name;
  private String description;
  private ChannelType type;
  private List<UUID> participantIds;
  private Instant lastMessageAt;

  public ChannelDto(Channel channel, List<UUID> participantIds, Instant lastMessageAt) {
    this.name = channel.getName();
    this.description = channel.getDescription();
    this.id = channel.getId();
    this.type = channel.getType();
    this.participantIds = (participantIds == null) ? new ArrayList<>() : participantIds;
    this.lastMessageAt = lastMessageAt;
  }

  @Override
  public String toString() {
    if (this.type == ChannelType.PUBLIC) {
      return "PUBLIC Channel[Name: " + this.getName() +
          " Description: " + this.getDescription() +
          " ID: " + this.id + " ]";
    } else if (this.type == ChannelType.PRIVATE) {
      return "PRIVATE Channel[" +
          " ID: " + this.id + "]" +
          "\nParticipants: " + participantIds;
    } else {
      return "";
    }
  }
}
