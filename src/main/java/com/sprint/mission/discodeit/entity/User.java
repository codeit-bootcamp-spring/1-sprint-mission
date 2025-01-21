package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter @Setter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String email;
    private String phoneNumber;
    private String addr;
    private int age;
    private String hobby;
    private ArrayList<String> interest;


    public User(String username, String email, String phoneNumber, String addr, int age, String hobby, ArrayList<String> interest){
        super();
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.addr = addr;
        this.age = age;
        this.hobby = hobby;
        this.interest = interest;
    }
}
