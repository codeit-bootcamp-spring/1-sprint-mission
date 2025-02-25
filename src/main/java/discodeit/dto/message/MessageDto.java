package discodeit.dto.message;

import discodeit.entity.BinaryContent;
import discodeit.entity.Message;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder
public record MessageDto(UUID id,
                         Instant createdAt,
                         String messageDetail,
                         UUID sendUserId,
                         UUID channelId,
                         List<UUID> attachedIds,
                         Instant updatedAt) {

    public static MessageDto fromDomain(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .messageDetail(message.getMessageDetail())
                .sendUserId(message.getSendUserId())
                .channelId(message.getChannelId())
                .attachedIds(message.getAttachmentIds())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}
