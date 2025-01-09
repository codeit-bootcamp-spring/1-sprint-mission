package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String name;
    private String email;
    private String status;

    public User(String name, String email, String status) {
        super();
        this.name = name;
        this.email = email;
        this.status = status;
    }

    //getter
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getStatus() {
        return status;
    }



    //update
    public void updateName(String name) {
        this.name = name;
    }

    public void updateEmail(String email) {
        this.email = email;
        getUpdatedAt();
    }
    public void updateStatus(String status) {
        this.status = status;
        getUpdatedAt();
    }


}
