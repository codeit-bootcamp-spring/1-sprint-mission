package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseUpdatableEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private UserStatus status;

    public User(String username, String email, String password, BinaryContent profile) {
        this.username = username;
        this.email = email;
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        this.password = Base64.getEncoder().encodeToString(hashedPassword.getBytes(StandardCharsets.UTF_8));

        this.profile = profile;
    }

    public void update(BinaryContent profile, String name, String email, String password) {
        updateProfile(profile);
        updateName(name);
        updateEmail(email);
        updatePassword(password);
    }

    public void updateProfile(BinaryContent profile) {
        if (profile.getId() == null || this.profile.getId().equals(profile.getId())) {
            return;
        }
        this.profile = profile;
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
