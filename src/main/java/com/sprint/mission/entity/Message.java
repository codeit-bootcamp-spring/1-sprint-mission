package com.sprint.mission.entity;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Instanceof;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Message implements Serializable, Comparable<Message> {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String firstId;
    private final UUID userId;
    private final UUID channelId;

    @Setter
    private String message;

    private final Instant createAt;

    @Setter
    private Instant updateAt;

    // 무조건 메시지는 CREATE로 생성하도록
    protected Message(UUID channelId, UUID userId, String message) {
        this.userId = userId;
        this.channelId = channelId;
        this.message = message;
        id = UUID.randomUUID();
        firstId = id.toString().split("-")[0];
        createAt = Instant.now();
    }

    public static Message createMessage(UUID channelId, UUID userId, String message) {
        return new Message(channelId, userId, message);
    }

    public void removeMessage(){

    }

    // 나중에, equals hashcode

    @Override
    public int compareTo(Message otherMessage) {
        // createdAt기준으로 정렬
        return this.createAt.compareTo(otherMessage.createAt);
    }
}
