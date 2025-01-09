package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.serviece.JCFMessageService;
import com.sprint.mission.discodeit.serviece.MessageService;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    private UUID ID;
    private Long createdAt;
    private Long updatedAt;
    private String Name;
    private ArrayList<Channel> channels=new ArrayList<>();
    private MessageService personal_message=new JCFMessageService();

    //뭐 더있나
    public User(String Name){
        this.ID= UUID.randomUUID();
        this.createdAt= System.currentTimeMillis();
        this.updatedAt=(long)0;
        this.Name=Name;
    };
    public UUID getId() {
        return ID;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
    public String getName(){
        return Name;
    }
    public void setId(UUID ID){
        this.ID=ID;
    }
    public void setCreatedAt(long createdAt) {
        this.createdAt= createdAt;
    }

    public void setUpdatedAt() {
        this.updatedAt= System.currentTimeMillis();
    }
    public void setName(String Name){
        this.Name= Name;
    }

    public String DisplayInfo(){
        return "ID: "+getId()+" Name: "+getName()+" createdAt: "+getCreatedAt()+" updatedAt: "+getUpdatedAt();
    }


}
