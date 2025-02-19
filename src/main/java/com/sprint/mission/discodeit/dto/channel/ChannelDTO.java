package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.domain.PrivateChannel;
import com.sprint.mission.discodeit.domain.PublicChannel;
import com.sprint.mission.discodeit.util.type.ChannelFormat;
import com.sprint.mission.discodeit.util.type.ChannelType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

public class ChannelDTO {
    @Getter
    @Builder
    public static class PublicChannelDTO {
        private UUID id;
        private Instant createdAt;
        private Instant updatedAt;
        private String name;
        private String description;
        private ChannelFormat channelFormat;
        private ChannelType channelType;
        private Instant latestMessageTime;

        public static PublicChannelDTO fromDomain(PublicChannel channel, Instant latestMessageTime) {
            return ChannelDTO.PublicChannelDTO.builder()
                    .id(channel.getId())
                    .createdAt(channel.getCreatedAt())
                    .updatedAt(channel.getUpdatedAt())
                    .name(channel.getName())
                    .description(channel.getDescription())
                    .channelFormat(channel.getChannelFormat())
                    .channelType(ChannelType.PUBLIC)
                    .latestMessageTime(latestMessageTime)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class PrivateChannelDTO {
        private UUID id;
        private Instant createdAt;
        private Instant updatedAt;
        private ChannelFormat channelFormat;
        private ChannelType channelType;
        private Instant latestMessageTime;

        public static PrivateChannelDTO fromDomain(PrivateChannel channel, Instant latestMessageTime) {
            return ChannelDTO.PrivateChannelDTO.builder()
                    .id(channel.getId())
                    .createdAt(channel.getCreatedAt())
                    .updatedAt(channel.getUpdatedAt())
                    .channelFormat(channel.getChannelFormat())
                    .channelType(ChannelType.PRIVATE)
                    .latestMessageTime(latestMessageTime)
                    .build();
        }
    }
}
