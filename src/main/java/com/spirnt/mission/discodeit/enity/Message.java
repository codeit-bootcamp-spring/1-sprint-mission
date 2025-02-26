package com.spirnt.mission.discodeit.enity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Message extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private UUID senderId;
    private UUID channelId;
    private List<UUID> attachedFiles;

    public Message(String content, UUID channelId, UUID senderId, List<UUID> attachedFiles) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.senderId = senderId;
        this.attachedFiles = (attachedFiles == null) ? new ArrayList<>() : attachedFiles;
    }


    public void update(String content) {
        boolean anyValueUpdated = false;
        if (content != null && !content.equals(this.content)) {
            this.content = content;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updateClass(Instant.now());
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + this.getId() + '\'' +
                ", content='" + this.content + '\'' +
                '}';
    }

    // 객체를 UUID로 비교하기 위해
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Message message = (Message) obj;
        return Objects.equals(this.getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
