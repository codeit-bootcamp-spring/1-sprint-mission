package com.spirnt.mission.discodeit.entity;

import com.spirnt.mission.discodeit.validation.Validator;
import com.spirnt.mission.discodeit.validation.Impl.ValidatorImpl;

import java.util.ArrayList;

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
        setUsername(username);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setAddr(addr);
        setAge(age);
        setHobby(hobby);
        setInterest(interest);
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setEmail(String email){ this.email = email; }
    public void setPhoneNumber(String phoneNumber){ this.phoneNumber = phoneNumber; }
    public void setAddr(String addr){ this.addr = addr; }
    public void setAge(int age){ this.age = age; }
    public void setHobby(String hobby){ this.hobby = hobby; }
    public void setInterest(ArrayList<String> interest){ this.interest = interest; }

    public String getUsername() { return username; }
    public String getEmail(){ return email; }
    public String getPhoneNumber() { return phoneNumber;}
    public String getAddr() { return addr; }
    public int getAge() { return age; }
    public String getHobby() { return hobby; }
    public ArrayList<String> getInterest() { return interest; }

}
