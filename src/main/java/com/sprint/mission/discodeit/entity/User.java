package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String username;
    private String email;
    private String password;
    private UUID binaryContentId;

    public User(String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void updateBinaryContentId(UUID binaryContentId) {
        this.binaryContentId = binaryContentId;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    //validator 에서 중복 검사 했으니 그냥 set만 하면 될듯
    public void setUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        setUpdatedAt();
    }

    public void setUsername(String username) {
        if(username ==null && username.equals(this.username)){
            throw new IllegalArgumentException("입력한 값이 null 혹은 중복입니다.");
        }
        this.username = username;
        setUpdatedAt();
    }

    public void setEmail(String email) {
        if(email ==null && email.equals(this.email)){
            throw new IllegalArgumentException("입력한 값이 null 혹은 중복입니다.");
        }
        this.email = email;
        setUpdatedAt();
    }

    public void setPassword(String password) {
        if(password ==null && password.equals(this.password)){
            throw new IllegalArgumentException("입력한 값이 null 혹은 중복입니다.");
        }
        this.password = password;
        setUpdatedAt();
    }

    public boolean userCompare(UUID id){
        if(id.equals(this.id)) return true;
        else return false;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

