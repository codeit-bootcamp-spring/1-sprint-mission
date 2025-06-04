package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.time.Instant;
import java.util.List;

public record MessageResponseDto(
    String id, // messageId
    Instant createdAt,
    Instant updatedAt,
    String content,
    String channelId,

    UserDto author,
    List<BinaryContentDto> attachments

) {

}
