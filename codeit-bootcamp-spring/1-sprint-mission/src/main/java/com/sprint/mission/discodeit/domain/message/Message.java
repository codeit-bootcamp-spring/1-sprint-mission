package com.sprint.mission.discodeit.domain.message;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.message.exception.InvalidMessageContentException;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = -5528993606626641717L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private final User sender;
    private final Channel destinationChannel;
    private String content;

    public Message(User sender, Channel destinationChannel, String content) {
        validate(content);
        this.id = UUID.randomUUID();
        this.sender = sender;
        this.destinationChannel = destinationChannel;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public boolean isSender(User user) {
        return sender.equals(user);
    }

    public void updateContent(String content) {
        validate(content);
        this.content = content;
    }

    private void validate(String content) {
        if (Objects.isNull(content) || content.isBlank()) {
            throw new InvalidMessageContentException(ErrorCode.INVALID_MESSAGE_CONTENT_NOT_NULL, content);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getSenderName() {
        return sender.getNicknameValue();
    }

    public UUID getDestinationChannelId() {
        return destinationChannel.getId();
    }

    public Channel getDestinationChannel() {
        return destinationChannel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
