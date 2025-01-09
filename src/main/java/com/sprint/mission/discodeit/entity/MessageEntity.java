package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class MessageEntity extends CommonEntity{
    // 유저 ID
    private UUID userId;
    // 채널 ID
    private UUID channelId;
    // 내용
    private String content;
    // 메시지 삭제 플래그 messageDelFlg ( 0 : default, 1 : deleted )
    private String messageDelFlg;

    public MessageEntity(UUID senderId, UUID channelId, String content) {
        super();
        this.userId = senderId;
        this.channelId = channelId;
        this.content = content;
        this.messageDelFlg = "0";
    }

    public UUID getUserEntity() {
        return userId;
    }

    public void updateUserId(UUID senderId) {
        this.userId = senderId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public void updateChannelId(UUID channelId) {
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public String getMessageDelFlg() {
        return messageDelFlg;
    }

    public void updateMessageDelFlg(String flg) {
        this.messageDelFlg = flg;
    }
}
