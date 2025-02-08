package com.sprint.mission.discodeit.domain.channel;

import com.sprint.mission.discodeit.domain.channel.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNameInvalidException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Channel {

    private final static int SUBJECT_MAX_LENGTH = 1024;

    private final UUID id;
    private String name;
    private String subject;
    private ChannelType type;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Channel(
            String name,
            ChannelType type
    ) {
        validate(name);
        this.id = UUID.randomUUID();
        this.name = name;
        this.subject = "";
        this.type = type;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public void updateSubject(String subject) {
        if (subject.length() > SUBJECT_MAX_LENGTH) {
            throw new IllegalArgumentException();
        }
        this.subject = subject;
    }

    private void validate(String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new ChannelNameInvalidException(ErrorCode.INVALID_CHANNEL_NAME_LENGTH, name);
        }
    }

    public UUID getId() {
        return id;
    }
}
