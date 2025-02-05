package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import java.util.List;

public record CreateMessageDto(
    String userId,
    String channelId,
    String content,
    List<BinaryContentDto> binaryContent
) {
}
