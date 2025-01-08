package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String username;
    private String email;

    // 생성자
    public User(String username, String email) {
        super(); // BaseEntity의 생성자 호출
        this.username = username;
        this.email = email;
    }



}
