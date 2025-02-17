package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String content;

    private final UUID channelId;
    private final UUID authorId;

    public Message(String content, UUID channelId, UUID authorId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.updatedAt = this.createdAt;
        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
    }

    public void update(String newContent) {
        this.content = newContent;
        this.updatedAt = Instant.now(); // 수정 시간을 갱신
    }

    @Override
    public String toString() {
        return "Message{\n" +
                "UUId=" + id +
                ", \ndestinationChannel ID : " + channelId +
                ", \ncreatedAt : " + createdAt +
                ", \nupdatedAt : " + updatedAt +
                ", \ncontent : " + content + '\'' +
                ", \nsendUser ID : " + authorId + "\n}";
    }

}
