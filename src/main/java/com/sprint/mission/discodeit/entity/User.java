package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String name;
    private String email;

    public User(String name, String email) {
        super();
        this.name = name;
        this.email = email;
    }

    //getter
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

    //update
    public void updateName(String name) {
        this.name = name;
        update();
    }

    public void updateEmail(String email) {
        this.email = email;
        update();
    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }



}
