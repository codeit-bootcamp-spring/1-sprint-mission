package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.message.MessageNullOrEmptyArgumentException;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 5909559788184597939L;

    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    //
    private final String content;
    //
    private final UUID channelId;
    private final UUID authorId;
    private final List<UUID> attachmentIds;

    public static Message createMessage(String content, UUID channelId, UUID authorId,
        List<UUID> attachmentIds) {
        return new Message(UUID.randomUUID(), Instant.now(), null, content, channelId, authorId,
            attachmentIds);
    }

    public Message update(String content) {
        if (content == null || content.isBlank()) {
            throw new MessageNullOrEmptyArgumentException("Message content cannot be null or empty");
        }

        if (content.equals(this.content)) {
            return this;
        }

        return new Message(
            this.id,
            this.createdAt,
            Instant.now(),
            content,
            this.channelId,
            this.authorId,
            this.attachmentIds
        );
    }
}
