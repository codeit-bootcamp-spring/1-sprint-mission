package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String email;
    private String password;
    private UUID profileImageId;
    private UUID userStatusId;
    private boolean online;

    public User(String name, String email, String password, UUID profileImageId, UUID userStatusId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImageId = profileImageId;
        this.userStatusId = userStatusId;
    }

    public void updatePassword(String oldPassword, String newPassword) {
        if (!this.password.equals(oldPassword) || oldPassword.equals(newPassword)) {
            throw new ServiceException(ErrorCode.PASSWORD_MISMATCH);
        }
        this.password = newPassword;
        this.updatedAt = Instant.now();
    }

    public void updateProfile(UUID profileImageId, UUID newProfileImageId) {
        if (!this.profileImageId.equals(profileImageId) || profileImageId.equals(newProfileImageId)) {
            throw new ServiceException(ErrorCode.INVALID_PROFILE);
        }
        this.profileImageId = newProfileImageId;
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) { // User 객체는 UUID, name, phone 3개의 필드가 동일하면 같은 유저라고 판단한다.
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}