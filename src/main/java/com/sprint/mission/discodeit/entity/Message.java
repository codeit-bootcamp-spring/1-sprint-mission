package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private final User author;
    private String content;

    public Message(String content, User author) {
        super();
        this.content = content;
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void update(String content) {
        this.content = content;
        update();
    }
}
