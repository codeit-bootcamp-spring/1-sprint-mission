package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private String content;
    private User author;   // 메시지를 작성한 사용자 ID
    private Channel channel; // 메시지가 속한 채널 ID

    public Message(String content, User author, Channel channel) {
        super();
        this.content = content;
        this.author = author;
        this.channel = channel;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
        updateTime();
    }

    public User getAuthor() {
        return author;
    }

    public void updateAuthor(User author) {
        this.author = author;
        updateTime();
    }

    public Channel getChannel() {
        return channel;
    }

    public void updateChannel(Channel channel) {
        this.channel = channel;
        updateTime();
    }

    @Override
    public String toString() {
        String authorName = (author != null) ? author.getUsername() : "Unknown";
        String channelName = (channel != null) ? channel.getName() : "Unknown";

        return "Message{" +
                "id='" + getId() + '\'' +
                "content='" + content + '\'' +
                ", author='" + authorName + '\'' +
                ", channel='" + channelName + '\'' +
                '}';
    }
}
