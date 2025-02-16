package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String username;
    private String email;
    private UserStatus status;
    private Map<UUID, ReadStatus> readStatuses;
    private BinaryContent profileImage;

    public User(String username, String email) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.createdAt = Instant.now().toEpochMilli();
        this.updatedAt = createdAt;
    }

    public void updateUsername(String username) {
        this.username = username;
        this.updatedAt = Instant.now().toEpochMilli();
    }

    public void updateProfileImage(BinaryContent newProfileImage) {
        this.profileImage = newProfileImage;
        this.updatedAt = Instant.now().toEpochMilli();
    }

    public void updateUserStatus(UserStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = Instant.now().toEpochMilli();
    }
}
