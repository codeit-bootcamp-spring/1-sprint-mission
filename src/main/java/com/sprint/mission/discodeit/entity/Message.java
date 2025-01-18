package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final String content;
    private final UUID userId;
    private final UUID channelId;

    public Message(String content, UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
    }

    // Getter 메서드 추가
    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public UUID getUserId() { // getUserId 메서드 추가
        return userId;
    }

    public UUID getChannelId() { // getChannelId 메서드 추가
        return channelId;
    }

    public void updateContent(String newContent) {
        // content 필드가 final인 경우 이를 변경하려면 새로운 객체를 생성해야 합니다.
        // 또는 content 필드를 final에서 제거하고 setter 메서드를 추가해야 합니다.
    }
}
