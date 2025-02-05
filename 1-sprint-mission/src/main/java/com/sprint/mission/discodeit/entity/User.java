package com.sprint.mission.discodeit.entity;
import java.util.UUID;

public class User {
    final private UUID id;
    final  private Long createdAt;
    private Long updatedAt;
    private String name;



    private User(String name){
        this.id=UUID.randomUUID();
        this.createdAt= System.currentTimeMillis();
        this.updatedAt=null;
        this.name=name;
    };

    private User(UUID id, Long createdAt, Long updatedAt, String name){
        this.id=id;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.name=name;

    }

    public static User createDefaultUser(String name){
        return new User(name);
    }
    public static User createUserAll(UUID id, Long createdAt, Long updatedAt, String name){
        return new User(id,createdAt,updatedAt,name);
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

    public void updateUpdatedAt() {
        this.updatedAt= System.currentTimeMillis();
    }
    public void updateName(String name){
        this.name= name;
    }


    @Override
    public String toString(){
        return "ID: "+getId()+" Name: "+getName()+" createdAt: "+getCreatedAt()+
                " updatedAt: "+(getUpdatedAt() == null ? "없음" : String.valueOf(getUpdatedAt()))+"\n";
    }


}
