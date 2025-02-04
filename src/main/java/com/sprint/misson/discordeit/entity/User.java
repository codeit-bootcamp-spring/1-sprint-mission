package com.sprint.misson.discordeit.entity;

import com.sprint.misson.discordeit.dto.UserDTO;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    //객체 식별용 id
    private final String id;
    //닉네임
    private String nickname;
    //이메일 - 로그인용 계정 아이디
    private String email;
    //비밀번호 -
    private transient String password;
    //생성 날짜 - 유닉스 타임스탬프
    private final Instant createdAt;
    //수정 시간
    private Instant updatedAt;

    //부가 기능 관련 필드
    //접속 상태
    private UserStatus userStatus;

    //사용자 설정 상태 메세지
    private String statusMessage;

    //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
    private AccountStatus accountStatus;

    public User(String nickname, String email, String password, UserStatus userStatus, String statusMessage, AccountStatus accountStatus) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.userStatus = userStatus;
        this.statusMessage = statusMessage;
        this.accountStatus = accountStatus;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //사용자가 생성된 이후, 생성 시간을 변경할 수 없으므로 update 미구현

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String toFullString() {
        return this.toShortString() + " / updatedAt: " + updatedAt + " / accountStatus: " + accountStatus + " / statusMessage: " + statusMessage;
    }

    public String toShortString() {
        return "[User] id: " + id + " / nickname: " + nickname + " / email:  " + email + " / userStatus: " + userStatus + " / createdAt: " + createdAt;
    }

    public void displayShortInfo() {
        System.out.println(toShortString());
    }

    public void displayFullInfo() {
        System.out.println(toFullString());
    }

    public boolean isUpdated(UserDTO userDTO) {
        if (userDTO == null) {
            return false;
        }

        boolean isUpdated = false;

        if (!nickname.equals(userDTO.getNickname()) && userDTO.getNickname() != null && !userDTO.getNickname().isEmpty()) {
            setNickname(userDTO.getNickname());
            isUpdated = true;
        }
        if (!email.equals(userDTO.getEmail()) && userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            setEmail(userDTO.getEmail());
            isUpdated = true;
        }
        if (!userStatus.equals(userDTO.getUserStatus()) && userDTO.getUserStatus() != null) {
            setUserStatus(userDTO.getUserStatus());
            isUpdated = true;
        }
        if (!accountStatus.equals(userDTO.getAccountStatus()) && userDTO.getAccountStatus() != null) {
            setAccountStatus(userDTO.getAccountStatus());
            isUpdated = true;
        }
        return isUpdated;
    }
}
