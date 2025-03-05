package com.sprint.mission.discodeit.dto.message;

public record CreateMessageDto(
    String content,
    String channelId,
    String authorId
) {

}
