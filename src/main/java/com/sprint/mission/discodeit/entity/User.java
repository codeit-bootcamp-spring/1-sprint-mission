package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable, Entity {
    private static final long serialVersionUID = 1L;

    private final Instant createdAt;
    @Setter private Instant updatedAt;
    private UUID id;
    @Setter private String email;
    @Setter private String userName;
    @Setter private String password;
    @Setter private String profilePicture;

    public User(String userName, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.updatedAt = Instant.now();
    }

    public User(String userName, String email, String password, String profilePicture) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.updatedAt = Instant.now();
        this.profilePicture = profilePicture;
    }


    //업데이트시간 수정
    public void setUpdatedAt(){this.updatedAt = Instant.now();}
    //유저이름 변경
    public void setUserName(String userName) {
        this.userName = userName;
        this.setUpdatedAt();
    }
    //유저 이메일 변경
    public void setEmail(String email) {
        this.email = email;
        this.setUpdatedAt();
    }
    //유저 비밀번호 변경
    public void setPassword(String password) {
        this.password = password;
        this.setUpdatedAt();
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        this.setUpdatedAt();
    }

    public void deleteProfilePicture() {
        this.profilePicture = null;
        this.setUpdatedAt();
    }
}
