package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User extends BaseUpdatableEntity {
    // TODO: profileId 변수 및 관련 로직 제거
    private UUID profileId;

    private String username;
    private String email;
    private String password;

    private BinaryContent profile;
    private UserStatus status;

    public User(String username, String email, String password, BinaryContent profile, UserStatus status) {
        this.username = username;
        this.email = email;
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        this.password = Base64.getEncoder().encodeToString(hashedPassword.getBytes(StandardCharsets.UTF_8));

        this.profile = profile;
        this.status = status;
    }

    public void update(UUID binaryContentId, String name, String email, String password) {
        updateBinaryContentId(binaryContentId);
        updateName(name);
        updateEmail(email);
        updatePassword(password);
    }

    public void updateBinaryContentId(UUID profileId) {
        if (profileId == null || this.profileId.equals(profileId)) {
            return;
        }
        this.profileId = profileId;
    }

    public void updateName(String username) {
        if (username.isBlank() || this.username.equals(username)) {
            return;
        }
        this.username = username;
    }

    public void updateEmail(String email) {
        if (email.isBlank() || this.email.equals(email)) {
            return;
        }
        this.email = email;
    }

    public void updatePassword(String newPassword) {
        String decodedPassword = new String(Base64.getDecoder().decode(this.password), StandardCharsets.UTF_8);
        if (newPassword.isBlank() || BCrypt.checkpw(newPassword, decodedPassword)) {
            return;
        }
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        this.password = Base64.getEncoder().encodeToString(hashedPassword.getBytes(StandardCharsets.UTF_8));
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
