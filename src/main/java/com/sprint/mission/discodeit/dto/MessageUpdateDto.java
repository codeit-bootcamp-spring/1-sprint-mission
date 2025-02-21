package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MessageUpdateDto (
    @NotNull
    String messageId,
    @NotNull
    String userId,
    @NotBlank
    String content,
    List<BinaryContentDto> binaryContent
){
}
