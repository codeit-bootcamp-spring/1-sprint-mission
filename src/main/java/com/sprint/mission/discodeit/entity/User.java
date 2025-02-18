package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.validation.Email;
import com.sprint.mission.discodeit.validation.Password;
import com.sprint.mission.discodeit.validation.PhoneNumber;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {             // 유저 정보

    @Serial
    private static final long serialVersionUID = 1L;

    // 공통 필드
    private final UUID id;            // pk
    private final Long createdAt;     // 생성 시간
    private Long updatedAt;           // 수정 시간

    // User 필드
    private final Email email;             // 이메일(아이디)
    private final Password password;          // 비밀번호
    private String name;              // 이름
    private String nickname;          // 닉네임
    private final PhoneNumber phoneNumber;       // 휴대폰 번호


    // 생성자
    public User(String email, String password, String name, String nickname, String phoneNumber, UserRepository userRepository) {
        // 생성자
        // 공통 필드 초기화
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();

        // User 필드
        this.email = new Email(email, userRepository);
        this.password = new Password(password);
        validationAndSetName(name);
        validationAndSetNickname(nickname);
        this.phoneNumber = new PhoneNumber(phoneNumber);
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
        return email.toString();
    }

    public String getPassword() {
        return password.toString();
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber.toString();
    }



    // update 함수
    public void updateEmail(String email) {
        this.email.updateEmail(email);
        updateUpdateAt();
    }

    public void updatePassword(String password) {
        this.password.updatePassword(password);
        updateUpdateAt();
    }

    public void updateName(String name) {
        validationAndSetName(name);
        updateUpdateAt();
    }

    public void updateNickname(String nickname) {
        validationAndSetNickname(nickname);
        updateUpdateAt();
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber.updatePhoneNumber(phoneNumber);
        updateUpdateAt();
    }

    public void updateUpdateAt(){
        this.updatedAt = System.currentTimeMillis();
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