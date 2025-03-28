package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

public record UpdateMessageDto(
    @NotBlank
    String userId,
    @NotBlank
    String newContent,
    @NotNull
    Instant updatedAt,
    @Size(max = 5)
    List<String> binaryContentIds
) {

}
