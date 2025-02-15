package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;

    private Instant updatedAt;
    private UUID binaryContentId;

    private String name;
    private String email;
    private transient String password;

    public User(UUID binaryContentId, String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.binaryContentId = binaryContentId;

        this.name = name;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public void updateBinaryContentId(UUID binaryContentId) {
        this.binaryContentId = binaryContentId;
        updateUpdatedAt();
    }

    public void update(String name, String email, String password) {
        boolean updated = updateName(name) || updateEmail(email) || updatePassword(password);
        if (updated) {
            updateUpdatedAt();
        }
    }

    public boolean updateName(String name) {
        if (name.isBlank() || this.name.equals(name)) {
            return false;
        }
        this.name = name;
        return true;
    }

    public boolean updateEmail(String email) {
        if (name.isBlank() || this.email.equals(email)) {
            return false;
        }
        this.email = email;
        return true;
    }

    public boolean updatePassword(String newPassword) {
        if (newPassword.isBlank() || BCrypt.checkpw(newPassword, password)) {
            return false;
        }
        this.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        return true;
    }

    public void validateDuplicateName(String name) {
        if (this.name.equals(name)) {
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
                name + "님의 정보입니다." + System.lineSeparator()
                        + "Name: " + name + System.lineSeparator()
                        + "Email: " + email + System.lineSeparator()
        );
    }
}
