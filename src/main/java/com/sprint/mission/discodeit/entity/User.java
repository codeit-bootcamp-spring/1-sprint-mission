package com.sprint.mission.discodeit.entity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable, Entity {
    private static final long serialVersionUID = 1L;

    //패스워드 제외한 모든 항목
    private final Instant createdAt;
    private Instant updatedAt;
    private UUID id;
    private String email;
    private String userName;
    private UUID profilePictureId;
    private String password;
    private UUID userStatusId;

    public User(String userName, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userName = userName;
        this.email = email;
        this.updatedAt = Instant.now();
        this.password = password;
        this.userStatusId = userStatusId;
    }

    //업데이트시간 수정
    public void setUpdatedAt(){this.updatedAt = Instant.now();}

    //유저이름 변경
    public void setUserName(String newUserName){
        this.userName = newUserName;
        this.setUpdatedAt();
    }
    //유저 이메일 변경
    public void setEmail(String newEmail) {
        this.email = newEmail;
        this.setUpdatedAt();
    }
    //프로필사진 변경
    public void setProfilePicture(UUID profilePictureId) {
        this.profilePictureId = profilePictureId;
        this.setUpdatedAt();
    }

}
