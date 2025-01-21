package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String channelName;
    private final User admin;
    private final List<Message> messagesList;
    private final List<User> memberList;


    public Channel(String channelName, User admin){
        id = UUID.randomUUID();
        createdAt=System.currentTimeMillis();
        updatedAt=null;
        messagesList = new ArrayList<>();
        memberList = new ArrayList<>();
        this.channelName = channelName;
        this.admin = admin;
        memberList.add(admin);
    }

    public Long getCreatedAt(){
        return createdAt;
    }

    public Long getUpdatedAt(){
        return updatedAt;
    }

    public UUID getId(){
        return id;
    }

    public String getChannelName(){
        return  channelName;
    }

    public User getAdmin(){
        return admin;
    }

    public List<Message> getMessagesList(){
        return messagesList;
    }

    public List<User> getMemberList(){
        return memberList;
    }

    public void addMember(User user){
        memberList.add(user);
    }

    public void addMessage(Message message){
        messagesList.add(message);
    }

    public void deleteMember(User user){
        memberList.remove(user);
    }

    public void deleteAllMember(){
        memberList.clear();
    }

    public void deleteMessage(Message message){
        messagesList.remove(message);
    }

    public void deleteAllMessage(){
        messagesList.clear();
    }

    public void setChannelName(String channelName){
        this.channelName = channelName;
        updatedAt = System.currentTimeMillis();
    }


}
