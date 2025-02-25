package discodeit.dto.channel;

import discodeit.entity.Channel;
import discodeit.entity.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record PrivateChannelDto(UUID id,
                               Instant createdAt,
                               ChannelType type,
                               List<UUID> participantIds,
                               Instant lastMessageAt) {

    public static PrivateChannelDto fromDomain(Channel channel, Instant lastMessageAt) {
        return PrivateChannelDto.builder()
                .id(channel.getId())
                .createdAt(channel.getCreatedAt())
                .type(channel.getType())
                .participantIds(channel.getParticipantIds())
                .lastMessageAt(lastMessageAt)
                .build();
    }
}