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

    private String name;
    private String email;
    private transient String password;

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.name = name;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public void update(String name, String email) {
        boolean updated = updateName(name) || updateEmail(email);
        if (updated) {
            updateUpdatedAt();
        }
    }

    public boolean updateName(String name) {
        if (this.name.equals(name)) {
            return false;
        }
        this.name = name;
        return true;
    }

    public boolean updateEmail(String email) {
        if (this.email.equals(email)) {
            return false;
        }
        this.email = email;
        return true;
    }

    public void updatePassword(String originalPassword, String newPassword) {
        if (!BCrypt.checkpw(originalPassword, password) || BCrypt.checkpw(newPassword, password)) {
            throw new IllegalArgumentException("[ERROR] 비밀번호 변경에 실패하였습니다.");
        }
        this.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }

    public void isDuplicateEmail(String email) {
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
