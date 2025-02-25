package discodeit.dto.channel;

import discodeit.entity.Channel;
import discodeit.entity.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record PublicChannelDto(UUID id,
                               Instant createdAt,
                               String channelName,
                               ChannelType type,
                               String description,
                               List<UUID> participantIds,
                               Instant lastMessageAt) {

    public static PublicChannelDto fromDomain(Channel channel, Instant lastMessageAt) {
        return PublicChannelDto.builder()
                .id(channel.getId())
                .createdAt(channel.getCreatedAt())
                .channelName(channel.getChannelName())
                .type(channel.getType())
                .description(channel.getDescription())
                .participantIds(channel.getParticipantIds())
                .lastMessageAt(lastMessageAt)
                .build();
    }

}
