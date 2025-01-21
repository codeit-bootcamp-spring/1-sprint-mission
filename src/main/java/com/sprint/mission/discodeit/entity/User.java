package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.validation.Validator;
import com.sprint.mission.discodeit.validation.Impl.ValidatorImpl;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class User extends BaseEntity{
    private final Validator validator = new ValidatorImpl();
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
