package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.user.UserNullOrEmptyArgumentException;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 7537036279213702953L;

    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    //
    private final String username;
    private final String email;
    private final String password;
    private final UUID profileId;   // BinaryContent

    public static User createUser(String username, String email, String password, UUID profileId) {
        return new User(UUID.randomUUID(), Instant.now(), null, username, email, password,
            profileId);
    }

    public User update(String username, String email, String password, UUID profileId) {
        if (username == null || username.isBlank()) {
            throw new UserNullOrEmptyArgumentException("User username cannot be null or empty");
        }
        if (email == null || email.isBlank()) {
            throw new UserNullOrEmptyArgumentException("User email cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new UserNullOrEmptyArgumentException("User password cannot be null or empty");
        }
        if (profileId == null) {
            throw new UserNullOrEmptyArgumentException("User profileId cannot be null or empty");
        }

        if (username.equals(this.username) &&
            email.equals(this.email) &&
            password.equals(this.password) &&
            profileId.equals(this.profileId)) {
            return this;
        }

        return new User(
            this.id,
            this.createdAt,
            Instant.now(),
            username,
            email,
            password,
            profileId
        );
    }
}
