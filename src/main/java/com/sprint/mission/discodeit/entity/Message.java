package com.sprint.mission.discodeit.entity;


import java.io.Serial;
import java.io.Serializable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createdAt;
    private final UUID writer; // link key with User
    private Long updatedAt;
    private String contents;
    private final UUID source; // link key with channel.


    public static class Builder {
        final UUID id;
        private final Long createdAt;
        private final UUID writer;
        private Long updatedAt;
        private String contents;
        private final UUID source;

        public Builder(UUID writer, UUID source) {
            this.id = UUID.randomUUID();
            this.createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            this.writer = writer;
            this.source = source;
        }

        public Builder buildUpdatedAt() {
            this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            return this;
        }

        public Builder buildContents(String contents) {
            this.contents = contents;
            return this;
        }


        public Message build() {
            if (writer == null || source == null) {
                throw new IllegalArgumentException("must fill in writer, source");
            }
            return new Message(this);
        }
    }

    private Message(Message.Builder builder) {
        this.id = builder.id;
        this.createdAt = builder.createdAt;
        this.writer = builder.writer;
        this.updatedAt = builder.updatedAt;
        this.contents = builder.contents;
        this.source = builder.source;
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

    public UUID getSource() {
        return source;
    }
    public UUID getWriter() {return writer;}

    public String getContents() {
        return contents;
    }

    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "msg created at : " + getCreatedAt() + ", updated at : " + getUpdatedAt() + ", msg id : "+ getId().toString().substring(0, 5) + ", msg writer : " + getWriter() + ", contents : " + getContents() + ", from : " + getSource();
    }

}
