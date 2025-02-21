package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends Common implements Serializable {
    private static final long serialVersionUID = 1L; //직렬화 버전
    //필드
    @Id
    @GeneratedValue
    private UUID id;

    private String username; //유저이름
    private String email; //이메일(아이디)
    private transient String password;//비밀번호
    private String phoneNumber;//전화번호
    private String address; //주소

    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private BinaryContent profileImage;//프로필 이미지

    //ReadStatus와 다대일 관계
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ReadStatus> readStatuses = new ArrayList<>();
    //UserStatus와 일대일 관계
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus userStatus;

    //사용자 검사 필드(클래스에서 변경되지 않는 공용 데이터)
    // 전화번호 010-0000-0000 만 허용
    private static final Pattern PHONE_PATTERN = Pattern.compile("010-\\d{4}-\\d{4}");
    // 기본적인 이메일 형식
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    //비밀번호 : 숫자 하나 이상, 알파벳 하나 이상, 특수문자 하나 이상, 8자 이상
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$");

    //생성자
    public User(String username, String email, String password, String phoneNumber, String address, BinaryContent profileImage){
        super();
        //검증
        if (username == null || username.length() > 20) {
            throw new IllegalArgumentException("이름은 최대 20자까지 가능합니다.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("비밀번호는 최소 8자이며 숫자, 문자, 특수문자를 포함해야 합니다.");
        }
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("휴대폰 번호 형식이 올바르지 않습니다. (예: 010-1234-5678 또는 01012345678)");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("주소는 비어 있을 수 없습니다.");
        }
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {//비밀번호 제외
        return String.format(
                "User { username=%s , email=%s , phoneNumber=%s , address=%s , createdAt=%s , updatedAt=%s }",
                username, email, phoneNumber, address,getCreatedAt(),getUpdatedAt()
        );
    }

    // update 메소드
    public void update(String newUsername, String newEmail, String newPassword, String newPhoneNumber, String newAddress,BinaryContent newProfile) {
        if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(this.username)) {
            this.username = newUsername;
            updateTimestamp();
        }
        if (newEmail != null && !newEmail.trim().isEmpty() && !newEmail.equals(this.email)) {
            this.email = newEmail;
            updateTimestamp();
        }
        if (newPassword != null && !newPassword.trim().isEmpty() && !newPassword.equals(this.password)) {
            this.password = newPassword;
            updateTimestamp();
        }
        if(newPhoneNumber !=null && !newPassword.trim().isEmpty() && !newPhoneNumber.equals(this.phoneNumber)){
            this.phoneNumber = newPhoneNumber;
            updateTimestamp();
        }
        if (newAddress != null && !newAddress.trim().isEmpty() && !newAddress.equals(this.address)) {
            this.address = newAddress;
            updateTimestamp();
        }
        if (newProfile != null){
            this.profileImage = newProfile;
            updateTimestamp();
        }
    }

}
