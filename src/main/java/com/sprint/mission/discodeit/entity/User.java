package com.sprint.mission.discodeit.entity;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private UUID profileId;
    private String username;
    private String email;
    private String password;

    public User(UUID profileId, String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.profileId = profileId;
        this.username = username;
        this.email = email;
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        this.password = Base64.getEncoder().encodeToString(hashedPassword.getBytes(StandardCharsets.UTF_8));
    }

    public void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public void update(UUID binaryContentId, String name, String email, String password) {
        boolean updated = false;
        if (updateBinaryContentId(binaryContentId)) {
            updated = true;
        }
        if (updateName(name)) {
            updated = true;
        }
        if (updateEmail(email)) {
            updated = true;
        }
        if (updatePassword(password)) {
            updated = true;
        }

        if (updated) {
            updateUpdatedAt();
        }
    }

    public boolean updateBinaryContentId(UUID profileId) {
        if (profileId == null || this.profileId.equals(profileId)) {
            return false;
        }
        this.profileId = profileId;
        return true;
    }

    public boolean updateName(String username) {
        if (username.isBlank() || this.username.equals(username)) {
            return false;
        }
        this.username = username;
        return true;
    }

    public boolean updateEmail(String email) {
        if (email.isBlank() || this.email.equals(email)) {
            return false;
        }
        this.email = email;
        return true;
    }

    public boolean updatePassword(String newPassword) {
        String decodedPassword = new String(Base64.getDecoder().decode(this.password), StandardCharsets.UTF_8);
        if (newPassword.isBlank() || BCrypt.checkpw(newPassword, decodedPassword)) {
            return false;
        }
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        this.password = Base64.getEncoder().encodeToString(hashedPassword.getBytes(StandardCharsets.UTF_8));
        return true;
    }

    public boolean isSameName(String name) {
        return this.username.equals(name);
    }

    public boolean isSamePassword(String password) {
        String decodedPassword = new String(Base64.getDecoder().decode(this.password), StandardCharsets.UTF_8);
        return BCrypt.checkpw(password, decodedPassword);
    }

    public void validateDuplicateName(String name) {
        if (this.username.equals(name)) {
            throw new IllegalArgumentException("[ERROR] 이미 존재하는 이름입니다.");
        }
    }

    public void validateDuplicateEmail(String email) {
        if (this.email.equals(email)) {
            throw new IllegalArgumentException("[ERROR] 이미 존재하는 이메일입니다.");
        }
    }

    @Override
    public String toString() {
        return String.format(
                username + "님의 정보입니다." + System.lineSeparator()
                        + "Name: " + username + System.lineSeparator()
                        + "Email: " + email + System.lineSeparator()
        );
    }
}
