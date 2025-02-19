package com.sprint.mission.discodeit.dto.channelDto;

import com.sprint.mission.discodeit.entity.Channel;
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

    public FindPrivateChannelResponseDto(Channel channel) {
        this.id = channel.getId();
        this.ownerId = channel.getOwnerId();
        this.lastMessageTime = channel.getLastMessageTime();
        this.members = channel.getMembers();
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
}
