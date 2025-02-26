package com.sprint.mission.discodeit.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
public class User implements Serializable {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    @NotBlank
    private String userName;
    @NotBlank
    private String userEmail;
    @NotBlank
    private transient String password;
    @NotBlank
    private String loginId;

    private BinaryContent attachProfile;


    public User(String loginId, String password, String userName, String userEmail) {
        this.id=UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.userEmail = userEmail;
    }
    public User(UUID id,String loginId, String password, String userName, String userEmail) {
        this.id=id;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public User(String loginId, String password, String userName, String userEmail,BinaryContent attachProfile) {
        this.id=UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.userEmail = userEmail;
        this.attachProfile = attachProfile;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setAttachProfile(BinaryContent attachProfile) {
        this.attachProfile = attachProfile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", password='" + password + '\'' +
                ", loginId='" + loginId + '\'' +
                ", attachProfile=" + attachProfile +
                '}';
    }
}
