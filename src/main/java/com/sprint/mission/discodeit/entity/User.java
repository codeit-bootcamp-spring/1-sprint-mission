package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;
    private transient String password;

    public User(String name,String email, String password) {
        super();
        this.name = name;
        this.email=email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void update(String name,String email ,String password) {
        boolean flag=false;
        if(name!=null&&!name.equals(this.name)){
            this.name = name;
            flag=true;
        }
        if(email!=null&&!email.equals(this.email)){
            this.email = email;
            flag=true;
        }
        if (password!=null&&!password.equals(this.password)){
            this.password = password;
            flag=true;
        }
        if(flag){
            update();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
