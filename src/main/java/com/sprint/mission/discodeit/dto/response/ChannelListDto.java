package com.sprint.mission.discodeit.dto.response;

import java.util.List;
import java.util.UUID;

public record ChannelListDto(
    List<UUID> channelIds,
    int totalCount
) {

  public static ChannelListDto from(List<UUID> channelIds) {
    return new ChannelListDto(channelIds, channelIds.size());
  }
}