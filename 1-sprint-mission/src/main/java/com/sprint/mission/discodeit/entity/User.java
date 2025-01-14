package com.sprint.mission.discodeit.entity;

public class User extends Data {

    private String userName;
    private String userId;
    private Long createdAt;
    private Long updatedAt;

    public User(String userName, String userId) {
        super();
        this.userName = userName;
        this.userId = userId;
        this.createdAt = System.currentTimeMillis(); // 객체 생성 시간
        this.updatedAt = this.createdAt;           // 초기 생성 시간과 동일
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.updatedAt = System.currentTimeMillis(); // 수정 시간 갱신
    }

    public void setUserId(String userId) {
        this.userId = userId;
        this.updatedAt = System.currentTimeMillis(); // 수정 시간 갱신
    }
}