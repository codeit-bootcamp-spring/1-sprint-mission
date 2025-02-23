package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public record MessageUpdateDTO(
        //TODO: uuid를 여기서 안받고, RequestParam을 통해 넘겨주는 방법? 중복으로 uuid를 받는 것 같다.
        UUID uuid,
        String content,
        //TODO: Sprint 3 binaryContent -> DTO or UUID 처리
        List<BinaryContent> binaryContentList
)
{
}
