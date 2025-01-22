package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {             // 유저 정보
    // 공통 필드
    private final UUID id;            // pk
    private final Long createdAt;     // 생성 시간
    private Long updatedAt;           // 수정 시간

    // User 필드
    private String email;             // 이메일(아이디)
    private String password;          // 비밀번호
    private String name;              // 이름
    private String nickname;          // 닉네임
    private String phoneNumber;       // 휴대폰 번호


    // 생성자
    public User(String email, String password, String name, String nickname, String phoneNumber){      // 생성자
        // 공통 필드 초기화
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        
        // User 필드
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }


    // Getter 함수
    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    // update 함수
    public void updateEmail(String email) {
        this.email = email;
        updateUpdateAt();
    }

    public void updatePassword(String password) {
        this.password = password;
        updateUpdateAt();
    }

    public void updateName(String name) {
        this.name = name;
        updateUpdateAt();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        updateUpdateAt();
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        updateUpdateAt();
    }

    public void updateUpdateAt(){
        this.updatedAt = System.currentTimeMillis();
    }
}