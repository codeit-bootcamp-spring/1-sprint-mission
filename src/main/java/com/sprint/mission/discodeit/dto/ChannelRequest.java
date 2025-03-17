package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record ChannelRequest(
) {

  public record CreatePublic(
      String name,
      String description
  ) {

  }

  public record CreatePrivate(
      List<UUID> participantIds
  ) {

  }

  public record Update(
      String name,
      String description
  ) {

  }
}
