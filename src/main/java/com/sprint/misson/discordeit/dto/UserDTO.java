package com.sprint.misson.discordeit.dto;

import com.sprint.misson.discordeit.entity.AccountStatus;
import com.sprint.misson.discordeit.entity.UserStatus;

public class UserDTO {

    //닉네임
    private String nickname;
    //이메일 - 로그인용 계정 아이디
    private String email;
    //접속 상태
    private UserStatus userStatus;
    //사용자 설정 상태 메세지
    private String statusMessage;
    //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
    private AccountStatus accountStatus;


    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public AccountStatus getAccountStatus(){
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus=accountStatus;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage=statusMessage;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus=userStatus;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public void setNickname(String nickname) {
        this.nickname=nickname;
    }

}


