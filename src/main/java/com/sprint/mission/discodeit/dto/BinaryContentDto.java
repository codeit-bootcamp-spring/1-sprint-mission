package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.constant.BinaryType;
import java.util.UUID;

public record BinaryContentDto(
    UUID uploadedById,
    BinaryType type
) {
}
