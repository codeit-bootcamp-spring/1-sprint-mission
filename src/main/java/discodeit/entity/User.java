package discodeit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class User {
    private UUID id;
    private Instant createdAt;

    private String username;
    private String email;
    private String phoneNum;
    private String password;
    private UUID profileId;
    private Instant updatedAt;

    protected User() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public User(String username, String email, String phoneNum, String password, UUID profileId) {
        this();
        this.username = username;
        this.email = email;
        this.phoneNum = phoneNum;
        this.password = password;
        this.profileId = profileId;
    }

    // Setter
    public void updatePassword(String oldPassword, String newPassword) {
        if (!oldPassword.equals(this.password)) {
            throw new IllegalArgumentException("[error] 비밀번호가 일치하지 않습니다.");
        }
        if (newPassword == null) {
            throw new IllegalArgumentException("[error] 비밀번호가 입력되지 않았습니다.");
        }
        if (newPassword.equals(this.password)) {
            throw new IllegalArgumentException("[error] 현재 비밀번호와 동일합니다.");
        }
        this.password = newPassword;
        this.updatedAt = Instant.now();
    }

    public void updatePhoneNum(String newPhoneNum) {
        if (newPhoneNum == null) {
            throw new IllegalArgumentException("[error] 전화번호가 입력되지 않았습니다.");
        }
        if (newPhoneNum.equals(this.phoneNum)) {
            throw new IllegalArgumentException("[error] 현재 전화번호와 동일합니다.");
        }
        this.phoneNum = newPhoneNum;
        this.updatedAt = Instant.now();

    }

    public void updateProfile(UUID newProfileId) {
        if (newProfileId != null) {
            this.profileId = newProfileId;
            this.updatedAt = Instant.now();
        }
    }


}
