package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.enums.EmailSubscriptionStatus;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID id;
    private final Nickname nickname;
    private final Nickname username;
    private final Email email;
    private final Password password;
    private final BirthDate birthDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final EmailSubscriptionStatus emailSubscriptionStatus;

    public User(
            Nickname nickname,
            Nickname username,
            Email email,
            Password password,
            BirthDate birthDate,
            EmailSubscriptionStatus emailSubscriptionStatus
    ) {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.emailSubscriptionStatus = emailSubscriptionStatus;
    }

    public UUID getId() {
        return id;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public Email getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Nickname getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public BirthDate getBirthDate() {
        return birthDate;
    }

    public EmailSubscriptionStatus getEmailSubscriptionStatus() {
        return emailSubscriptionStatus;
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
