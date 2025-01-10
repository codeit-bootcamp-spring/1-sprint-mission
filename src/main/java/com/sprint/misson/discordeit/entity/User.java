package com.sprint.misson.discordeit.entity;

import java.util.UUID;

public class User {
    //객체 식별용 id
    private final UUID id;
    //닉네임
    private String nickname;
    //이메일 - 로그인용 계정 아이디
    private String email;
    //생성 날짜 - 유닉스 타임스탬프
    private final Long createdAt;
    //수정 시간
    private Long updatedAt;

    //부가 기능 관련 필드
    //접속 상태
    private UserStatus userStatus;

    //사용자 설정 상태 메세지
    private String statusMessage;

    //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
    private AccountStatus accountStatus;

    public User(String nickname, String email, UserStatus userStatus, String statusMessage, AccountStatus accountStatus) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.nickname = nickname;
        this.email = email;
        this.userStatus = userStatus;
        this.statusMessage = statusMessage;
        this.accountStatus = accountStatus;
    }


    public UUID getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    //사용자가 생성된 이후, 생성 시간을 변경할 수 없으므로 update 미구현
    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt=System.currentTimeMillis();
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus=accountStatus;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage=statusMessage;
    }


}
