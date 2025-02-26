package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.FilePathContents;
import com.sprint.mission.discodeit.vo.Email;
import com.sprint.mission.discodeit.vo.Password;
import com.sprint.mission.discodeit.vo.PhoneNumber;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {             // 유저 정보

    @Serial
    private static final long serialVersionUID = 1L;

    // 공통 필드
    private final UUID id;                  // pk
    private final Instant createdAt;        // 생성 시간
    private Instant updatedAt;              // 수정 시간

    // User 필드
    private Email email;                    // 이메일
    private Password password;              // 비밀번호
    private String name;                    // 이름(로그인 시 필요한 아이디)
    private String nickname;                // 닉네임
    private PhoneNumber phoneNumber;        // 휴대폰 번호
    private UUID profileImageId;     // 프로필 사진
    private final UserStatus userStatus;    // 유저 접속 상태

    // 생성자
    public User(String email, String password, String name, String nickname, String phoneNumber, UUID profileImageId) throws IOException {
        // 생성자
        // 공통 필드 초기화
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        
        // User 필드
        this.email = new Email(email);
        this.password = new Password(password);
        validationAndSetName(name);
        validationAndSetNickname(nickname);
        this.phoneNumber = new PhoneNumber(phoneNumber);
        this.userStatus = new UserStatus(this.id);

        this.profileImageId = profileImageId;
    }


    // update 함수
    public void updateEmail(String updateEmail) {
        if (!updateEmail.isBlank()){       // 수정 시에는 null 들어올 시 IllegalArgumentException이 뜨지 않고, 메서드가 무시되도록 하기 위해 if문 작성
            this.email = new Email(updateEmail);
            updateUpdateAt();
        }
    }

    public void updatePassword(String updatePassword) {
        if (!updatePassword.isBlank()){
            this.password = new Password(updatePassword);
            updateUpdateAt();
        }
    }

    public void updateName(String updateName) {
        if (!updateName.isBlank()){
            validationAndSetName(updateName);
            updateUpdateAt();
        }
    }

    public void updateNickname(String updateNickname) {
        if (!updateNickname.isBlank()){
            validationAndSetNickname(updateNickname);
            updateUpdateAt();
        }
    }

    public void updatePhoneNumber(String updatePhoneNumber) {
        if (!updatePhoneNumber.isBlank()){
            this.phoneNumber = new PhoneNumber(updatePhoneNumber);
            updateUpdateAt();
        }
    }

    public void updateProfileImageId(UUID profileImageId) throws IOException {
        if (profileImageId != null) {
            this.profileImageId = profileImageId;
            updateUpdateAt();
        }
    }

    public void updateUpdateAt(){
        this.updatedAt = Instant.now();
    }


    // 이름 유효성 검사 및 세팅
    private void validationAndSetName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름을 입력해주세요.");
        }

        name = name.trim();

        this.name = name;
    }

    // 닉네임 유효성 검사 및 세팅
    private void validationAndSetNickname(String nickname) {

        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        }

        nickname = nickname.trim();

        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "email=" + email +
                ", password=" + password +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}