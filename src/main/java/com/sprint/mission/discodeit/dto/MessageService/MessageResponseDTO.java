package com.sprint.mission.discodeit.dto.MessageService;

import com.sprint.mission.discodeit.dto.binaryContentService.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDTO(
        UUID id,                 // 메시지 ID
        UUID channelId,          // 채널 ID
        UUID authorId,           // 작성자 ID
        String content,          // 메시지 내용
        Instant createdAt,       // 메시지 생성 시간
        List<BinaryContentDTO> attachments // 첨부파일 리스트
) {
    public static MessageResponseDTO from(Message message, List<BinaryContentDTO> attachments) {
        return new MessageResponseDTO(
                message.getId(),
                message.getChannelId(),
                message.getAuthorId(),
                message.getContent(),
                message.getCreatedAt(),
                attachments
        );
    }
}
