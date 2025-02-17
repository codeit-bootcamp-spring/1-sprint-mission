package com.sprint.mission.discodeit.dto.messageDto;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FindMessageResponseDto {
    UUID id;
    UUID channelId;
    UUID writerId;
    String context;
    List<UUID> imagesId;
    Instant createAt;

    public FindMessageResponseDto(Message message) {
        this.id = message.getId();
        this.channelId = message.getChannelId();
        this.writerId = message.getWriterId();
        this.context = message.getContext();
        this.imagesId = message.getImagesId();
        this.createAt = message.getCreatedAt();
    }
}
