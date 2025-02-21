package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateMessageDto(
    @NotBlank
    String userId,
    @NotBlank
    String channelId,
    @NotBlank @Size(min = 1 , max = 1000)
    String content,
    List<BinaryContentDto> binaryContent
) {
}
