package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.UUID;

public class Channel {
    private UUID ID;
    private Long createdAt;
    private Long updatedAt;
    private String ChannelName;
    private ArrayList<User> userlist;
    private ArrayList<Message> messageslist;

    public Channel(String ChannelName){
        this.ID= UUID.randomUUID();
        this.createdAt= System.currentTimeMillis();
        this.updatedAt=(long)0;
        this.ChannelName=ChannelName;

        userlist=new ArrayList<>();
        messageslist=new ArrayList<>();
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
    public String getChannelName(){
        return ChannelName;
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
    public void setChannelName(String ChannelName){
        this.ChannelName= ChannelName;
    }

    public void addUser(ArrayList<User> list){
        userlist.addAll(list);
    }
    public void addUser(User list){
        userlist.add(list);
    }
    public void addMessage(Message m){
        messageslist.add(m);
    }

    public String DisplayChannelInfo(){
        String one="ChannelName: "+ChannelName+" ID: "+ID;
        String two="createdAt: "+createdAt+" updatedAt: "+updatedAt;
        return one+"\n"+two;
    }
}
