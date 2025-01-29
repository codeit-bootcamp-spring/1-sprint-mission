package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class User extends Common implements Serializable {
    private static final long serialVersionUID = 1L; //직렬화 버전
    //필드
    private String username; //유저이름
    private String email; //이메일(아이디)
    private transient String password;//비밀번호
    private String phoneNumber;//전화번호
    private String address; //주소

    //생성자
    public User(String username,String email,String password,String phoneNumber,String address){
        super();
        this.username=username;
        this.email=email;
        this.password=password;
        this.phoneNumber=phoneNumber;
        this.address=address;
    }
    //Getter
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getAddress() {
        return address;
    }


    @Override
    public String toString() {
        return "이름='" + username + '\'' +
                ", 이메일='" + email + '\'' +
                ", 비밀번호='" + password + '\'' +
                ", 전화번호='" + phoneNumber + '\'' +
                ", 주소='" + address + '\'' +
                ", 생성='"+ getCreatedAt();
    }

    public String toStringUpdate() {
        return "이름='" + username + '\'' +
                ", 이메일='" + email + '\'' +
                ", 비밀번호='" + password + '\'' +
                ", 전화번호='" + phoneNumber + '\'' +
                ", 주소='" + address + '\'' +
                ", 수정='"+ getUpdatedAt();
    }

    // update 메소드
    public void update(String username, String email, String password, String phoneNumber, String address) {
        if (username != null) this.username = username;
        if (email != null) this.email = email;
        if (password != null) this.password = password;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
        if (address != null) this.address = address;
        setUpdatedAt(Instant.now().getEpochSecond());
    }

}
