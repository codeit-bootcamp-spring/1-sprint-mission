package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
// 직렬화하려는 클래스가 Serializable 인터페이스를 구현해야 한다.
public class User extends BaseEntity implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        //
        private String username;
        private String email;
        private String password;
        private UUID profileId;

        public User(String username,
                    String email,
                    String password,
                    UUID profileId){
            this.username = username;
            this.email = email;
            this.password = password;
            this.profileId = profileId;
        }

    // BaseEntity 의 refreshUpdateAt() 를 도메인 차원에서 조절해야 하나?
    public void updateUsername(String username){
        if(username == null || username.trim().isEmpty()){
            throw new IllegalArgumentException("username 은 공백일 수 없습니다.");
        }
        this.username = username;
        this.refreshUpdateAt();
    }

    public void updateUserEmail(String email){
        if(email == null || !email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new IllegalArgumentException("email 이 잘못되었습니다.");
        }
        this.email = email;
        this.refreshUpdateAt();
    }

    public void updateUserPassword(String password){
        if(password == null || password.trim().isEmpty()){
            throw new IllegalArgumentException("password 가 잘못되었습니다.");
        }
        this.password = password;
        this.refreshUpdateAt();
    }

    public void updateProfileId(UUID profileId){
            if(profileId == null){
                throw new IllegalArgumentException("profileId 가 잘못디되었습니다.");
            }
            this.profileId = profileId;
            this.refreshUpdateAt();
    }
}
