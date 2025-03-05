package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.common.Name;
import com.sprint.mission.discodeit.entity.Channel;
import lombok.Builder;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChannelDTO {

    @Builder
    public record request(UUID owner, String serverName, String description, Channel.ChannelType channelType,
                          List<UUID> members, Instant recent) {
    }

    @Builder
    public record response(Long id, UUID uuid, UUID ownerId, Name serverName, String description,
                           List<UUID> members, Channel.ChannelType channelType, Instant recentMessageTime) {
        public static response from(Map.Entry<Long, Channel> channelEntry, Instant recentMessageTime) {
            Channel channel = channelEntry.getValue();
            return response.builder()
                    .id(channelEntry.getKey())
                    .uuid(channel.getId())
                    .ownerId(channel.getOwnerId())
                    .serverName(channel.getServerName())
                    .description(channel.getDescription())
                    .members(channel.getMembers().isEmpty() ? Collections.emptyList() : channel.getMembers())
                    .channelType(channel.getChannelType())
                    .recentMessageTime(recentMessageTime)
                    .build();
        }

        public static response from(Map.Entry<Long, Channel> channelEntry) {
            return from(channelEntry, null);
        }
    }

    @Builder
    public record update(Long id, UUID owner, String serverName, String description, Channel.ChannelType channelType,
                         List<UUID> members, Instant recent) {
    }
}
