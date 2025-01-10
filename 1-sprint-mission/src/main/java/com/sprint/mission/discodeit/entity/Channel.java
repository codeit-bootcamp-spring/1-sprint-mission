package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.UUID;

public class Channel {
    final private UUID id;
    final private Long createdAt;
    private Long updatedAt;
    private String ChannelName;
    private ArrayList<User> userlist;
    private ArrayList<Message> messageslist;

    public Channel(String ChannelName){
        this.id= UUID.randomUUID();
        this.createdAt= System.currentTimeMillis();
        this.updatedAt=null;
        this.ChannelName=ChannelName;

        userlist=new ArrayList<>();
        messageslist=new ArrayList<>();
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
    public String getChannelName(){
        return ChannelName;
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
        String one="ChannelName: "+ChannelName+" ID: "+id;
        String two="createdAt: "+createdAt+" updatedAt: "+(getUpdatedAt() == null ? "없음" : String.valueOf(getUpdatedAt()));
        return one+"\n"+two;
    }
}
