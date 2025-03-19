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
public class FindPublicChannelResponseDto implements FindChannelResponseDto {

    UUID id;
    UUID ownerId;
    String category;
    String name;
    String explanation;
    List<UUID> members;
    Instant lastMessageTime;
    ChannelType type;

    public static FindPublicChannelResponseDto fromEntity(Channel channel) {
        return new FindPublicChannelResponseDto(
                channel.getId(),
                channel.getOwner().getId(),
                channel.getCategory(),
                channel.getName(),
                channel.getDescription(),
                channel.getMembers().stream()
                        .map(BaseEntity::getId)
                        .toList(),
                channel.getLastMessageTime(),
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
    public String getName() {
        return name;
    }

    @Override
    public String getExplanation() {
        return explanation;
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
