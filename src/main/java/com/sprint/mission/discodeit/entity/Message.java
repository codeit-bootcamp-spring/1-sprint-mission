package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private String content;
    private String userId;   // 메시지를 작성한 사용자 ID
    private String channelId; // 메시지가 속한 채널 ID
    // 생성자
    public Message(String content, String userId, String channelId) {
        super(); // BaseEntity의 생성자 호출
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
    }

    // Getter 메서드
    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void updateContent(String content) {
        this.content = content;
        setUpdatedAt(System.currentTimeMillis());
    }

    public void updateUserId(String userId) {
        this.userId = userId;
        setUpdatedAt(System.currentTimeMillis());
    }

    public void updateChannelId(String channelId) {
        this.channelId = channelId;
        setUpdatedAt(System.currentTimeMillis());
    }
}
