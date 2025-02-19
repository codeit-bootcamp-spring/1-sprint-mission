package com.sprint.mission.discodeit.dto.MessageService;

import com.sprint.mission.discodeit.dto.binaryContentService.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public record MessageUpdateRequestDTO(
        UUID messageId,          // 수정할 메시지 ID
        String newContent,       // 수정할 메시지 내용
        List<UUID> fileIds       // 새롭게 등록할 첨부파일 ID 리스트 (선택적)
) {

}
