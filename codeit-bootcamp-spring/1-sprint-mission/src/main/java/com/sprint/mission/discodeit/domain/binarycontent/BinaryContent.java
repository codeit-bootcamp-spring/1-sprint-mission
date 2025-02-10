package com.sprint.mission.discodeit.domain.binarycontent;

import com.sprint.mission.discodeit.domain.binarycontent.enums.BinaryType;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.domain.user.User;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class BinaryContent {

    private final UUID id;
    private final Instant createdAt;
    private final User user;
    private final Message message;
    private final Asset asset;
    private final BinaryType binaryType;

    public BinaryContent(User user, Message message, Asset asset, BinaryType binaryType) {
        validate(user, message, binaryType);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.user = user;
        this.message = message;
        this.asset = asset;
        this.binaryType = binaryType;
    }

    private void validate(User user, Message message, BinaryType binaryType) {
        if (Objects.isNull(user) || Objects.isNull(message) || Objects.isNull(asset)) {
            throw new IllegalArgumentException();
        }
    }

}
