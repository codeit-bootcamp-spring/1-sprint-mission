package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public record ChannelUpdateDto(
    String newName,
    String newDescription
) {
}
