package com.sprint.mission.entity.main;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class Message implements Serializable, Comparable<Message> {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Channel writtenPlace;
    private final User writer;
    private final Instant createAt;
    private Instant updateAt;

    private BinaryMessageContent BinaryContent;
    private String content;

    // 무조건 메시지는 CREATE로 생성하도록
    private Message(Channel channel, User user, String message) {
        id = UUID.randomUUID();
        this.writtenPlace = channel;
        this.writer = user;
        this.content = message;
        createAt = Instant.now();
    }

    public static Message createMessage(Channel channel, User user, String message) {
        Message createdMessage = new Message(channel, user, message);
        channel.updateLastMessageTime(); // 채널에서 메시지 마지막 시간 초기화
        return createdMessage;
    }

    // 나중에, equals hashcode

    @Override
    public int compareTo(Message otherMessage) {
        // createdAt기준으로 정렬
        return this.createAt.compareTo(otherMessage.createAt);
    }
}
