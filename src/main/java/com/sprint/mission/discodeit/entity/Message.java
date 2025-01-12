package com.sprint.mission.discodeit.entity;

import java.util.UUID;

/** # Message
 * - 텍스트 채팅 객체
 * - 단체 대화만 한다고 가정 (언급 기능 등은 일단 고려하지 않음)
 * - 파일 첨부 기능 등이 아닌 단순 문자 채팅만 한다고 고려
 */

public class Message {
    private UUID id;
    private User author;    // 작성자
    private String content; // 메시지 내용
    private Channel channel;    // 메시지 대상 서버
    private boolean status;     // 메시지 상태 (삭제 시 false -> 일정 시간 후 hard delete)
    private long createdAt;
    private long updatedAt;

    // 생성자
    Message() {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
    }

    // Getter
    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
}
