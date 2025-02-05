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
    //접속 상태
    //todo - 고민
    //1. private String userStatusId로 할 경우
    // - 장점: 책임 분리 가능,
    // - 단점: 유저가 접속 중인지 아닌지 구분하기 위해 userStatusService에서 조회 -> isActive() 해서 온라인 상태 확인해야
    //2. private UserStatus userStatus 로 할 경우
    // - 장점: isActive()를 통해 바로 확인 가능
    // - 단점: 책임 명확하지 않을 것 같음, DB 변경 시 호환 문제 예상됨, 만약 나중에 JPA 사용 시 UserStatus를 업데이트하면 User도 업데이트 되지 않을까?(잘모름)
    private String userStatusId;

    //사용자 설정 상태 메세지
    private String statusMessage;
    //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
    private AccountStatus accountStatus;
    //사용자 프로필 사진
    private String profileImageId;

    public User(String nickname, String email, String password, String userStatusId, String statusMessage, AccountStatus accountStatus) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.userStatusId = userStatusId;
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

    public void setUserStatus(String userStatusId) {
        this.userStatusId = userStatusId;
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
        return "[User] id: " + id + " / nickname: " + nickname + " / email:  " + email + " / userStatusId: " + userStatusId + " / createdAt: " + createdAt;
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

        if (!nickname.equals(userDTO.nickname()) && userDTO.nickname() != null && !userDTO.nickname().isEmpty()) {
            setNickname(userDTO.nickname());
            isUpdated = true;
        }
        if (!email.equals(userDTO.email()) && userDTO.email() != null && !userDTO.email().isEmpty()) {
            setEmail(userDTO.email());
            isUpdated = true;
        }
        // UserStatus 분리로 삭제

        if (!accountStatus.equals(userDTO.accountStatus()) && userDTO.accountStatus() != null) {
            setAccountStatus(userDTO.accountStatus());
            isUpdated = true;
        }
        return isUpdated;
    }
}
