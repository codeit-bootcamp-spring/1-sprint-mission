package com.sprint.mission.discodeit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private UUID id;
    private String content;
    // 변경: senderId → authorId
    private UUID authorId;
    private UUID channelId;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            timezone = "UTC")
    private Instant updatedAt;

    private List<UUID> attachmentIds;

    // 엔티티(Message)로부터 변환하기 위한 생성자
    public MessageResponse(com.sprint.mission.discodeit.entity.Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.authorId = message.getAuthorId();
        this.channelId = message.getChannelId();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
        this.attachmentIds = message.getAttachmentIds();
    }

    // 5개 인자 생성자: createdAt, updatedAt은 null 또는 기본값으로 처리
    public MessageResponse(UUID id, String content, UUID authorId, UUID channelId, List<UUID> attachmentIds) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
