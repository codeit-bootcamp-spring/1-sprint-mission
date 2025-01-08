package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private String content;
    private User author;

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
