package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Message extends BaseEntity implements Serializable {
    private String content;
    private String author;
    public String channel;
    private static final long serialVersionUID = 1L;

    public Message(String content, String author, String channel) {
        super();
        this.content = content;
        this.author = author;
        this.channel = channel;
    }
    // getter
    public String getContent() {
        return content;
    }
    public String getAuthor() {
        return author;
    }
    public String getChannel() {
        return channel;
    }

    // update
    public void updateContent(String content) {
        this.content = content;
        update();
    }
    public void updateAuthor(String author) {
        this.author = author;
        update();
    }
    public void updateChannel(String channel) {
        this.channel = channel;
        update();
    }
    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", channel='" + channel + '\'' +

                '}';
    }
}
