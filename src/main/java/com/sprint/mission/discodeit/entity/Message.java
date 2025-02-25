package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;
    private UUID senderId;
    private UUID channelId;
    private List<UUID> attachmentIds = new ArrayList<>();

    // 기본 생성자: 새로운 메시지 생성 시 사용 (BaseEntity의 id, createdAt, updatedAt 사용)
    public Message(String content, UUID senderId, UUID channelId) {
        setId(UUID.randomUUID());
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
        this.attachmentIds = new ArrayList<>();
        setCreatedAt(Instant.now());
    }

    // 추가 생성자: 필요한 매개변수를 포함하여 메시지 생성
    public Message(UUID id, String content, UUID senderId, UUID channelId, Instant createdAt) {
        setId(id);
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
        this.attachmentIds = new ArrayList<>();
        setCreatedAt(createdAt);
    }

    public void addAttachment(UUID attachmentId) {
        this.attachmentIds.add(attachmentId);
        setUpdatedAt(Instant.now());
    }

    // JSON 응답 시 createdAt 값을 에포크 밀리초로 반환 (BaseEntity의 createdAt을 활용)
    @Override
    @JsonProperty("createdAt")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public Instant getCreatedAt() {
        return super.getCreatedAt();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + getId() +
                ", content='" + content + '\'' +
                ", senderId=" + senderId +
                ", channelId=" + channelId +
                ", attachmentIds=" + attachmentIds +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
