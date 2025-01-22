package com.sprint.mission.discodeit.entity;

import java.util.Date;
import java.util.UUID;

public class User extends Common{
    //필드
    private String username; //유저이름
    private String email; //이메일(아이디)
    private String password;//비밀번호
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
                ", 생성='"+ getFormattedCreatedAt();
    }

    public String toStringUpdate() {
        return "이름='" + username + '\'' +
                ", 이메일='" + email + '\'' +
                ", 비밀번호='" + password + '\'' +
                ", 전화번호='" + phoneNumber + '\'' +
                ", 주소='" + address + '\'' +
                ", 수정='"+ getFormattedUpdatedAt();
    }

    // update 메소드
    public void updateUserName(String username){
        this.username=username;
        setUpdatedAt(System.currentTimeMillis());
    }
    public void updateEmail(String email){
        this.email=email;
        setUpdatedAt(System.currentTimeMillis());
    }
    public void updatePassword(String password){
        this.password=password;
        setUpdatedAt(System.currentTimeMillis());
    }
    public void updatePhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
        setUpdatedAt(System.currentTimeMillis());
    }
    public void updateAddress(String address){
        this.address=address;
        setUpdatedAt(System.currentTimeMillis());
    }
}
