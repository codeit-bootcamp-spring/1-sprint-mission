package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public record ChannelResponse(
        String name,
        String description,
        List<UserResponse> member,
        UserResponse owner
) {
    public static ChannelResponse fromEntity(Channel channel){
        return new ChannelResponse(
            channel.getName(),
            channel.getDescription(),
            channel.getMember().stream()
                    .map(UserResponse::fromEntity)
                    .toList(),
            UserResponse.fromEntity(channel.getOwner())
        );
    }
}
