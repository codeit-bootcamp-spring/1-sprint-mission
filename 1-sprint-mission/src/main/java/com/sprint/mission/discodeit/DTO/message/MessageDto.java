package com.sprint.mission.discodeit.DTO.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public record MessageDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String title,
        String messageBody,
        List<UUID> binaryContentIds,
        String senderName,
        String receiverName,
        UUID senderId,
        UUID receiverId
) {
    public static MessageDto fromEntity(Message message) {
        return new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getTitle(),
                message.getMessageBody(),
                message.getBinaryContents().stream()
                        .map(content -> content.getId()) // BinaryContent 엔티티의 ID만 가져옴
                        .collect(Collectors.toList()),
                message.getSenderName(),
                message.getReceiverName(),
                message.getSenderId(),
                message.getReceiverId()
        );
    }

    public static List<MessageDto> fromEntity(List<Message> message) {
        return message.stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }
}