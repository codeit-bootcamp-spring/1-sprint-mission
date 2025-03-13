package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FindMessageResponseDto {
    UUID id;
    UUID channelId;
    UUID writerId;
    String context;
    List<UUID> imagesId;
    Instant createdAt;

    public static FindMessageResponseDto fromEntity(Message message) {
        return new FindMessageResponseDto(
                message.getId(),
                message.getChannel().getId(),
                message.getAuthor().getId(),
                message.getContent(),
                message.getAttachments().stream()
                        .map(BaseEntity::getId)
                        .toList(),
                message.getCreatedAt()
        );
    }
}
