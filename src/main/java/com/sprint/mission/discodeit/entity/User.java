package com.sprint.mission.discodeit.entity;

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
        if (username == null || email == null || password == null || profileId == null) {
            throw new IllegalArgumentException();
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
