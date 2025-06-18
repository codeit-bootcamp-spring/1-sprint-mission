package com.spirnt.mission.discodeit.dto.message;


import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageDto {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String content;
    private UUID channelId;
    private UserDto author;
    private List<BinaryContentDto> attachments;

    @Builder
    public MessageDto(UUID id, Instant createdAt, Instant updatedAt, String content, UUID channelId,
        UserDto author, List<BinaryContentDto> attachments) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
        this.channelId = channelId;
        this.author = author;
        this.attachments = attachments;
    }
}
