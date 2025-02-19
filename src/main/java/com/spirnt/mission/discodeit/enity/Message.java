package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Message extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private UUID senderId;
    private UUID channelId;

    public Message(String content, UUID channelId, UUID senderId) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.senderId = senderId;
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
