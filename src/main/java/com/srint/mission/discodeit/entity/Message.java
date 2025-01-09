package com.srint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity{

    private String msg;
    private final UUID userId;
    private final UUID channelId;


    public Message(UUID userId, UUID channelId, String msg) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msg='" + msg + '\'' +
                ", userId=" + userId +
                ", channelId=" + channelId +
                '}'+"  "+ super.toString();
    }
}
