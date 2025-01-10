package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class User {


    private final UUID uuID;
    private final Long createdAt;
    private Long updatedAt;
    private final String name;
    private String email;
    private String password;
    private String iD;


    //생성
    public User(String name, String email,String iD ,String password){
        this.uuID = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.name = name;
        this.email = email;
        this.iD = iD;
        this.password = password;
    }

    public UUID getuuID() {
        return uuID;
    }

    public Long getCreatedAt() {

        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getId() {
        return iD;
    }

    public void update(String email, String iD, String password){
        this.email = email;
        this.iD = iD;
        this.password = password;
        updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "User{\n" +
                "UUID=" + uuID +
                ", \ncreatedAt=" + sdf.format(new Date(createdAt)) +
                ", \nupdatedAt=" + sdf.format(new Date(updatedAt)) +
                ", \nname='" + name + '\'' +
                ", \nemail='" + email + '\'' +
                ", \nid='" + iD + '\'' +
                '}';
    }


}
