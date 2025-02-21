package com.sprint.mission.discodeit.dto.request.message;

import com.sprint.mission.discodeit.dto.request.binary.BinaryContentCreateDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record MessageUpdateDTO(
    UUID messageId,
    @NotBlank(message = "Content is required") String newContent,
    List<BinaryContentCreateDTO> attachments
) {

}
