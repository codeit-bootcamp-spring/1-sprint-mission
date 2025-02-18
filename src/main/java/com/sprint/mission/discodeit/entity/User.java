package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

@Getter @Setter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private UUID profileImageId;
//    private String addr;
//    private int age;
//    private String hobby;
//    private ArrayList<String> interest;

//String addr, int age, String hobby, ArrayList<String> interest
    public User(String username, String password, String email, String phoneNumber){
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
//        this.addr = addr;
//        this.age = age;
//        this.hobby = hobby;
//        this.interest = interest;
    }
}
