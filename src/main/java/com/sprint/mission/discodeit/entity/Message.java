package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private final Channel channelId;
    private final User senderUser;

    public Message(String content, Channel channelId, User senderUser) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.senderUser = senderUser;
    }

    public String getContent() {
        return content;
    }

    public Channel getChannelId() {
        return channelId;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void update(String content) {
        boolean flag=false;
        if(content!=null&&!content.equals(this.content)) {
            this.content = content;
            flag=true;
        }
        if (flag){
            update();
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", senderUser=" + senderUser +
                '}';
    }
}
