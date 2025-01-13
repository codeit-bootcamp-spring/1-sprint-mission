package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity{
    private String content;
    private User sender;

    public Message(String content, User sender) {
        super();
        this.content = content;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public void updateContent(String content) {
        this.content = content;
        super.update();
    }

    public void updateSender(User sender) {
        this.sender = sender;
        super.update();
    }
}
