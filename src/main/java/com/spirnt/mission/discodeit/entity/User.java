package com.spirnt.mission.discodeit.entity;

import com.spirnt.mission.discodeit.validation.EmailValidator;
import com.spirnt.mission.discodeit.validation.Impl.EmailValidatorImpl;

import java.util.Objects;

public class User extends BaseEntity{
    private String username;
    private String email;
    private final EmailValidator emailValidator = new EmailValidatorImpl();

    public User(String username, String email){
        super();
        this.username = username;
        setEmail(email);
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setEmail(String email){
        if(!emailValidator.isValid(email)){
            System.out.println("Invalid email format: " + email);
        }else{
            this.email = email;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getEmail(){
        return email;
    }

//    public void updateUsername(String username){
//        this.username = username;
//        update();
//    }
//
//    public void updateEmail(String email){
//
//        update();
//    }

}
