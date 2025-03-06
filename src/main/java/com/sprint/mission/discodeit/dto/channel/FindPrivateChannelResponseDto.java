package com.sprint.mission.discodeit.dto.channel;

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
    boolean isPublic;

    public static FindPrivateChannelResponseDto fromEntity(Channel channel) {
        return new FindPrivateChannelResponseDto(
                channel.getId(),
                channel.getOwnerId(),
                channel.getLastMessageTime(),
                channel.getMembers(),
                channel.isPublic()
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
    public boolean getIsPublic() {
        return isPublic;
    }
}
