package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.vo.Email;
import com.sprint.mission.discodeit.vo.Password;
import com.sprint.mission.discodeit.vo.PhoneNumber;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "users")
@Entity
@NoArgsConstructor(force = true)
public class User extends BaseUpdatableEntity implements Serializable {             // 유저 정보

    @Serial
    private static final long serialVersionUID = 1L;

    // User 필드
    @Column(name = "username")
    private String username;                // 로그인 시 필요한 아이디

    @Embedded
    private Email email;                    // 이메일

    @Embedded
    private Password password;              // 비밀번호

    @Column(name = "nickname")
    private String nickname;                // 닉네임

    @Embedded
    private PhoneNumber phoneNumber;        // 휴대폰 번호

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private BinaryContent profile;                 // 프로필 사진

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final UserStatus status;        // 유저 접속 상태

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Channel> ownedChannels;

    // 생성자
    public User(String email, String password, String username, String nickname, String phoneNumber, BinaryContent profile) throws IOException {

        this.email = new Email(email);
        this.password = new Password(password);
        validationAndSetName(username);
        validationAndSetNickname(nickname);
        this.phoneNumber = new PhoneNumber(phoneNumber);
        this.profile = profile;

        this.status = new UserStatus(this);
        this.ownedChannels = new ArrayList<>();
    }


    // update 함수
    public void updateEmail(String updateEmail) {
        if (!updateEmail.isBlank()){       // 수정 시에는 null 들어올 시 IllegalArgumentException이 뜨지 않고, 메서드가 무시되도록 하기 위해 if문 작성
            this.email = new Email(updateEmail);
        }
    }

    public void updatePassword(String updatePassword) {
        if (!updatePassword.isBlank()){
            this.password = new Password(updatePassword);
        }
    }

    public void updateUsername(String updateName) {
        if (!updateName.isBlank()){
            validationAndSetName(updateName);
        }
    }

    public void updateNickname(String updateNickname) {
        if (!updateNickname.isBlank()){
            validationAndSetNickname(updateNickname);
        }
    }

    public void updatePhoneNumber(String updatePhoneNumber) {
        if (!updatePhoneNumber.isBlank()){
            this.phoneNumber = new PhoneNumber(updatePhoneNumber);
        }
    }

    public void updateProfile(BinaryContent profile) throws IOException {
        if (profile != null) {
            this.profile = profile;
        }
    }

    public void addChannel(Channel channel) {
        this.ownedChannels.add(channel);
    }

    public void deleteChannel(Channel channel) {
        this.ownedChannels.remove(channel);
    }


    // 이름 유효성 검사 및 세팅
    private void validationAndSetName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름을 입력해주세요.");
        }

        name = name.trim();

        this.username = name;
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
                ", name='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}