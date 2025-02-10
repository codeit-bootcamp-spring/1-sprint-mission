package com.sprint.mission.discodeit.domain.message;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.message.exception.InvalidMessageContentException;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Message {

    private final UUID id;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final User sender;
    private final Channel destinationChannel;
    private String content;

    public Message(User sender, Channel destinationChannel, String content) {
        validate(content);
        this.id = UUID.randomUUID();
        this.sender = sender;
        this.destinationChannel = destinationChannel;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

}
