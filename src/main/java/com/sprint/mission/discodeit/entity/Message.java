package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Message {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String content;
    private User writer;
    private Channel channel;

    public Message(String content, User writer, Channel channel) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = null;
        this.content = content;
        this.writer = writer;
        this.channel = channel;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }

    public User getWriter() {
        return writer;
    }

    public Channel getChannel() {
        return channel;
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String createdAtFormatted = createdAt != null
                ? LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()).format(formatter)
                : "N/A";
        String updatedAtFormatted = updatedAt != null
                ? LocalDateTime.ofInstant(Instant.ofEpochMilli(updatedAt), ZoneId.systemDefault()).format(formatter)
                : "N/A";

        String writerUserName = writer != null ? writer.getName() : "Unknown";
        String useChannelName = channel != null ? channel.getName() : "Unknown";

        return "Message{" +
                "id=" + id +
                ", createdAt=" + createdAtFormatted +
                ", updatedAt=" + updatedAtFormatted +
                ", content='" + content + '\'' +
                ", writer='" + writerUserName + '\'' +
                ", channel='" + useChannelName + '\'' +
                '}';
    }
}
