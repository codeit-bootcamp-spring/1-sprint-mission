package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String userName;
    private String email;
    private String password;

    public User(String userName, String email, String password){
        id = UUID.randomUUID();
        createdAt=System.currentTimeMillis();
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public Long getCreatedAt(){
        return createdAt;
    }

    public Long getUpdatedAt(){
        return updatedAt;
    }

    public void setUpdatedAt(){
        updatedAt = System.currentTimeMillis();
    }

    public UUID getId(){
        return id;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
        updatedAt = System.currentTimeMillis();
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
        updatedAt = System.currentTimeMillis();
    }

    public void userInfo(){
        System.out.println("사용자 이름: " + userName + "\n이메일: " + email);
    }

    public String getPassword(){
        return password;
    }

}
