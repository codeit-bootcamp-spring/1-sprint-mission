package com.sprint.mission.discodeit.dto.request.message;

import com.sprint.mission.discodeit.dto.request.binary.BinaryContentCreateRequest;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

public record MessageCreateDTO(
    UUID userId,
    UUID channelId,
    @NotBlank(message = "Content is required") String content,
    List<BinaryContentCreateRequest> attachments
) {

}
