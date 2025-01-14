package com.sprint.mission.discodeit.entity;

public class User extends AbstractEntity {
    private String username;
    private String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public void update(String username, String email){
        this.username = username;
        this.email = email;
        updateTimestamp();
    }
}
