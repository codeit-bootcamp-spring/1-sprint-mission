package com.sprint.mission.discodeit.domain.channel;

import com.sprint.mission.discodeit.domain.channel.enums.ChannelType;
import com.sprint.mission.discodeit.domain.channel.enums.ChannelVisibility;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNameInvalidException;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelSubjectOverLengthException;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Channel implements Serializable {

    @Serial
    private static final long serialVersionUID = 6800939087441796647L;
    private final static int SUBJECT_MAX_LENGTH = 1024;

    private final UUID id;
    private String name;
    private String subject;
    private ChannelType type;
    private final Instant createdAt;
    private Instant updatedAt;
    private final User manager;
    private final ChannelVisibility visibility;
    private final ParticipatedUser participatedUser;

    private Channel(
            String name,
            ChannelType type,
            User manager,
            ChannelVisibility visibility
    ) {
        validate(name);
        this.id = UUID.randomUUID();
        this.name = name;
        this.subject = "";
        this.type = type;
        this.visibility = visibility;
        createdAt = Instant.now();
        updatedAt = Instant.now();
        this.manager = manager;
        participatedUser = new ParticipatedUser();
    }

    public static Channel ofPublicChannel(String name, ChannelType type, User user) {
        return new Channel(name, type, user, ChannelVisibility.PUBLIC);
    }

    public static Channel ofPrivateChannel(User manager, ChannelType type) {
        return new Channel("", type, manager, ChannelVisibility.PRIVATE);
    }

    public void join(User user) {
        participatedUser.addUser(user);
    }

    public void updateSubject(String subject) {
        if (subject.length() > SUBJECT_MAX_LENGTH) {
            throw new ChannelSubjectOverLengthException(ErrorCode.INVALID_SUBJECT_LENGTH,
                    "입력 글자 수:".concat(String.valueOf(subject.length())));
        }
        this.subject = subject;
    }


    public void updateName(String name) {
        this.name = name.trim();
    }

    public void quitChannel(User user) {
        participatedUser.removeUser(user);
    }

    public boolean isManager(User user) {
        return this.manager.equals(user);
    }

    public boolean isPublic() {
        return this.visibility == ChannelVisibility.PUBLIC;
    }

    public Set<UUID> getParticipantUserId() {
        return this.participatedUser.getParticipatedUserId();
    }

    private void validate(String name) {
        if (Objects.isNull(name)) {
            throw new ChannelNameInvalidException(ErrorCode.INVALID_CHANNEL_NAME_NOT_NULL, name);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Channel channel = (Channel) o;
        return Objects.equals(id, channel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
