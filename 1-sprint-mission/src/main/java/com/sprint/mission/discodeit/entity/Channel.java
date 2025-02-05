package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.UUID;

public class Channel {
    final private UUID id;
    final private Long createdAt;
    private Long updatedAt;
    private String channelName;
    private final ArrayList<User> userlist=new ArrayList<>();
    private final ArrayList<Message> messageslist=new ArrayList<>();


    private Channel(String channelName){
        this.id=UUID.randomUUID();
        this.createdAt= System.currentTimeMillis();
        this.updatedAt=null;
        this.channelName=channelName;
    };
    private Channel(UUID id,Long createdAt,Long updatedAt,String channelName){
        this.id=id;
        this.createdAt= createdAt;
        this.updatedAt=updatedAt;
        this.channelName=channelName;
    };
    public static Channel createDefaultChannel(String channelName){
        return new Channel(channelName);
    }

    public static Channel createChannelAll(UUID id,Long createdAt,Long updatedAt,String channelName){
        return new Channel(id,createdAt,updatedAt,channelName);
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
    public String getChannelName(){
        return channelName;
    }

    public void updateUpdatedAt() {
        this.updatedAt= System.currentTimeMillis();
    }
    public void updateChannelName(String ChannelName){
        this.channelName= ChannelName;
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

    public String toString(){
        StringBuilder display = new StringBuilder();
        display.append("ChannelName: ").append(channelName)
                .append(" ID: ").append(id)
                .append("\ncreatedAt: ").append(createdAt)
                .append(" updatedAt: ").append(getUpdatedAt() == null ? "없음" : String.valueOf(getUpdatedAt()))
                .append("\n");
        return display.toString();
    }
}
