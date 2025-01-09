package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.UuidGenerator;

import java.time.LocalDateTime;

public class User {
    private String UUID;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String profilePictureURL;
    private String description;
    private Long createdAt;
    private Long updatedAt;

    public User(String username,
                String password,
                String email,
                String nickname,
                String phoneNumber,
                String profilePictureURL,
                String description) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.profilePictureURL = profilePictureURL;
        this.description = description;
        this.UUID = UuidGenerator.generateUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUsername() {
        return username;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void updatePassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void updateProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public String getDescription() {
        return description;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
            "UUID='" + UUID + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            ", nickname='" + nickname + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", profilePictureURL='" + profilePictureURL + '\'' +
            ", description='" + description + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }
}
