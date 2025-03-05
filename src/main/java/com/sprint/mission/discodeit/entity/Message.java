package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * # Message
 * - 텍스트 채팅 객체
 * - 단체 대화만 한다고 가정 (언급 기능 등은 일단 고려하지 않음)
 * - 파일 첨부 기능 등이 아닌 단순 문자 채팅만 한다고 고려
 */

@Getter
public class Message implements Serializable {
    private UUID id;
    private UUID authorId;    // 작성자
    private String content; // 메시지 내용
    private UUID channelId;    // 메시지 대상 서버
    private boolean status;     // 메시지 상태 (삭제 시 false -> 일정 시간 후 hard delete)
    private Instant createdAt;
    private Instant updatedAt;

    // 생성자
    public Message(UUID authorId, UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
        this.status = true;
        refreshUpdatedAt();
    }

    public void updateContent(String content) {
        this.content = content;
        refreshUpdatedAt();
    }

    public void updateStatus(boolean status) {
        this.status = status;
        refreshUpdatedAt();
    }

    void refreshUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", author=" + authorId +
                ", content='" + content + '\'' +
                '}';
    }
}
