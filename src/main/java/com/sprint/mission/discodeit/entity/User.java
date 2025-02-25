package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    //객체 식별용 id
    private final String id;
    //로그인 아이디
    private String username;
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
    //사용자 설정 상태 메세지
    private String statusMessage;
    //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
    private AccountStatus accountStatus;
    //사용자 프로필 사진
    private String profileImageId;

    public User(String username, String nickname, String email, String password, String statusMessage, AccountStatus accountStatus, String profileImageId) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.statusMessage = statusMessage;
        this.accountStatus = accountStatus;
        this.profileImageId = profileImageId;
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

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setProfileImageId(String profileImageId) {
        this.profileImageId = profileImageId;
    }

    public String toFullString() {
        return this.toShortString() + " / updatedAt: " + updatedAt + " / accountStatus: " + accountStatus + " / statusMessage: " + statusMessage;
    }

    public String toShortString() {
        return "[User] id: " + id + " / nickname: " + nickname + " / email:  " + email + " / createdAt: " + createdAt;
    }

    public void displayShortInfo() {
        System.out.println(toShortString());
    }

    public void displayFullInfo() {
        System.out.println(toFullString());
    }

    public boolean isUpdated(UpdateUserDto updateUserDto) {
        if (updateUserDto == null) {
            return false;
        }

        boolean isUpdated = false;

        if (!nickname.equals(updateUserDto.nickname()) && updateUserDto.nickname() != null && !updateUserDto.nickname().isEmpty()) {
            setNickname(updateUserDto.nickname());
            isUpdated = true;
        }
        if (!email.equals(updateUserDto.email()) && updateUserDto.email() != null && !updateUserDto.email().isEmpty()) {
            setEmail(updateUserDto.email());
            isUpdated = true;
        }
        // UserStatus 분리로 삭제

        if (!accountStatus.equals(updateUserDto.accountStatus()) && updateUserDto.accountStatus() != null) {
            setAccountStatus(updateUserDto.accountStatus());
            isUpdated = true;
        }
        return isUpdated;
    }
}
