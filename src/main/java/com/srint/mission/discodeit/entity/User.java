package com.srint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class User implements Serializable {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();

        this.username = username;
        this.email = email;
        this.password = password;
    }


    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
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


    public static boolean validation(String username, String email, String password){
        if (username == null || username.isEmpty()) {
            return false;
        }
        if (email == null || email.isEmpty() || !email.matches("^[\\w.-]+@[\\w-]+\\.[a-zA-Z]{2,6}$")) {
            return false;
        }
        if (password == null || password.isEmpty()) {
            return false;
        }
        return true;
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

