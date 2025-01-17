package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private String content;
    private final User senderUser;//final로 변경

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

    public void update(String content) {
        this.content = content;
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
