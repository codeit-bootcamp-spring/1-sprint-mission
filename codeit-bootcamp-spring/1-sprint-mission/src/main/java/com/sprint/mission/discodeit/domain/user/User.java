package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.enums.EmailSubscriptionStatus;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1284229838745272170L;

    private final UUID id;
    private final Nickname nickname;
    private final Username username;
    private final Email email;
    private final Password password;
    private final BirthDate birthDate;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final EmailSubscriptionStatus emailSubscriptionStatus;

    public User(
            Nickname nickname,
            Username username,
            Email email,
            Password password,
            BirthDate birthDate,
            EmailSubscriptionStatus emailSubscriptionStatus
    ) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.emailSubscriptionStatus = emailSubscriptionStatus;
    }

    public void updatePassword(String encodedPassword) {
        password.changePassword(encodedPassword);
    }

    public UUID getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    public String getUsernameValue() {
        return this.username.getValue();
    }

    public String getNicknameValue() {
        return this.nickname.getValue();
    }

    public String getEmailValue() {
        return this.email.getValue();
    }

    public String getPasswordValue() {
        return this.password.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
