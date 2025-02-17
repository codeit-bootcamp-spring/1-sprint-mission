package com.sprint.mission.discodeit.dto.channelDto;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Setter
@AllArgsConstructor
public class FindPublicChannelResponseDto implements FindChannelResponseDto {

    UUID id;
    UUID ownerId;
    String name;
    String explanation;
    String category;
    Instant lastMessageTime;
    List<UUID> members;

    public FindPublicChannelResponseDto(Channel channel) {
        this.id = channel.getId();
        this.ownerId = channel.getOwnerId();
        this.name = channel.getName();
        this.explanation = channel.getExplanation();
        this.category = channel.getCategory();
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
    public String getCategory() {
        return category;
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
}
