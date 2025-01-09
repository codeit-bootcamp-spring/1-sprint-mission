package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private String content;
    private User senderUser;

    public Message(String content, User senderUser) {
        super();
        this.content = content;
        this.senderUser = senderUser;
    }

    public String getContent() {
        return content;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void update(String content, User senderUser) {
        this.content = content;
        this.senderUser = senderUser;
        update();
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", senderUser=" + senderUser +
                '}';
    }
}
