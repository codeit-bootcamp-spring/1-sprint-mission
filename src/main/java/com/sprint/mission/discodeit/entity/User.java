package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String name;
    private String password;

    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void update(String name, String password) {
        this.name = name;
        this.password = password;
        update();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
