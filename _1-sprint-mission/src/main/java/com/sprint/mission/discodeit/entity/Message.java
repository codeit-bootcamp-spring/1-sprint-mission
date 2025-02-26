package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Message extends Common implements Serializable {
    private static final long serialVersionUID = 1L; //직렬화 버전

    private String content; //메세지내용
    private UUID userId; // 메시지를 보낸 사용자
    private UUID channelId; // 메시지가 속한 채널

    //BinaryContent 참조 필드
    private List<UUID> attachmentIds;

    //생성자
    public Message(String content, UUID userId, UUID channelId, List<UUID> attachmentIds) {
        super(UUID.randomUUID(), Instant.now());
        // 검증
        if (content == null || content.trim().isEmpty() || content.length() > 500) {
            throw new IllegalArgumentException("메시지 내용은 1~500자 사이여야 합니다.");
        }
        if (channelId == null) {
            throw new IllegalArgumentException("채널 ID는 필수입니다.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("작성자 ID는 필수입니다.");
        }

        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds;
    }

    // update 메소드
    public void updateContent(String newContent) {
        if (newContent != null && !newContent.trim().isEmpty() && !newContent.equals(this.content)) {
            this.content = newContent;
            updateTimestamp();
        }
    }
}
