package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public record MessageUpdateDTO(
        UUID uuid,
        String content,
        //TODO: Sprint 3 binaryContent -> DTO or UUID 처리
        List<BinaryContent> binaryContentList
)
{
}
