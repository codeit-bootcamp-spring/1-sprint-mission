package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    final private UUID id;
    final  private Long createdAt;
    private Long updatedAt;
    private String name;


    //뭐 더있나
    public User(String name){
        this.id= UUID.randomUUID();
        this.createdAt= System.currentTimeMillis();
        this.updatedAt=null;
        this.name=name;
    };

    public static User createDefaultUser(String name){
        return new User(name);
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }
    public String getName(){
        return name;
    }

    public void setUpdatedAt() {
        this.updatedAt= System.currentTimeMillis();
    }
    public void setName(String name){
        this.name= name;
    }

    @Override
    public String toString(){
        return "ID: "+getId()+" Name: "+getName()+" createdAt: "+getCreatedAt()+
                " updatedAt: "+(getUpdatedAt() == null ? "없음" : String.valueOf(getUpdatedAt()));
    }


}
