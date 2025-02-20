package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    private final byte[] profileImageUrl;
    private final byte[] messageFileUrl;

    private final User user;  // User 객체를 참조
    private final Message msg;  // Message 객체를 참조

    // BinaryContent 객체에서 User와 Message의 ID를 반환하는 메소드
    public UUID getUserId() {
        return user.getId();
    }

    public UUID getMessageId() {
        return msg.getId();
    }

    // BinaryContent 객체를 출력하는 메소드
    @Override
    public String toString() {
        return "BinaryContent{" +
                "profileImageUrl='" + profileImageUrl + '\'' +
                ", messageFileUrl='" + messageFileUrl + '\'' +
                ", userId=" + getUserId() +
                ", messageId=" + getMessageId() +
                '}';
    }
}
