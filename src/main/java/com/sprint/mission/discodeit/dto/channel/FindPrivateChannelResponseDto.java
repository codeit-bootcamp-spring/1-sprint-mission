package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.util.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Setter
@AllArgsConstructor
public class FindPrivateChannelResponseDto implements FindChannelResponseDto {

    UUID id;
    UUID ownerId;
    Instant lastMessageTime;
    List<UUID> members;
    ChannelType type;

    public static FindPrivateChannelResponseDto fromEntity(Channel channel) {
        return new FindPrivateChannelResponseDto(
                channel.getId(),
                channel.getOwner().getId(),
                channel.getLastMessageTime(),
                channel.getMembers().stream()
                        .map(BaseEntity::getId)
                        .toList(),
                channel.getType()
        );
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getOwnerId() {
        return ownerId;
    }

    @Override
    public Instant getLastMessageTime() {
        return lastMessageTime;
    }

    @Override
    public List<UUID> getMembers() {
        return members;
    }

    @Override
    public ChannelType getType() {
        return type;
    }
}
