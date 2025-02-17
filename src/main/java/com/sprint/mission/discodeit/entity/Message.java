package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Builder.Default
    private Instant createdAt = Instant.now();  // Instant로 변경

    private Instant updatedAt;  // Instant로 변경
    private String content;
    private UUID channelId;
    private UUID authorId;

    // 새로 추가된 생성자
    public Message(String content, UUID channelId, UUID authorId) {
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.createdAt = Instant.now();  // 생성 시점에 설정
        this.updatedAt = this.createdAt;  // 생성 시점으로 초기화
    }

    // update 메서드에서 updatedAt 갱신
    public void update(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            this.updatedAt = Instant.now();  // 수정 시점에 updatedAt 갱신
        }
    }
}
