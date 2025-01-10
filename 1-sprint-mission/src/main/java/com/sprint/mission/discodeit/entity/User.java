package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.serviece.JCFMessageService;
import com.sprint.mission.discodeit.serviece.MessageService;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    final private UUID id;
    final  private Long createdAt;
    private Long updatedAt;
    private String Name;


    //뭐 더있나
    public User(String Name){
        this.id= UUID.randomUUID();
        this.createdAt= System.currentTimeMillis();
        this.updatedAt=null;
        this.Name=Name;
    };
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
        return Name;
    }

    public void setUpdatedAt() {
        this.updatedAt= System.currentTimeMillis();
    }
    public void setName(String Name){
        this.Name= Name;
    }

    public String DisplayInfo(){
        return "ID: "+getId()+" Name: "+getName()+" createdAt: "+getCreatedAt()+
                " updatedAt: "+(getUpdatedAt() == null ? "없음" : String.valueOf(getUpdatedAt()));
    }


}
