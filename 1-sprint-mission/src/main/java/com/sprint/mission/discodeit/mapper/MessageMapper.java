package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import java.util.List;
import java.util.UUID;

public class MessageMapper {

  public MessageDto toDto(Message entity) {
    if (entity == null) {
      return null;
    }

    List<UUID> attachmentIds = entity.getAttachments().stream()
        .map(BaseEntity::getId)
        .toList();

    return new MessageDto(
        entity.getId(),
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getContent(),
        entity.getChannel() != null ? entity.getChannel().getId() : null,
        entity.getAuthor() != null ? entity.getAuthor().getId() : null,
        attachmentIds
    );
  }

}
