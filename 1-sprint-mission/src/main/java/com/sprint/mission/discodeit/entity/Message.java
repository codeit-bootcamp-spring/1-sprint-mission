package com.sprint.mission.discodeit.entity;

import com.sun.nio.sctp.MessageInfo;

import java.util.UUID;

public class Message {
    private UUID ID;
    private Long createdAt;
    private Long updatedAt;
    private String title;
    private String MessageBody;

    private String senderName;
    private String reciverName;
    private UUID senderID;
    private UUID reciverID;

    public Message(String title,String MessageBody){
        this.ID= UUID.randomUUID();
        this.createdAt= System.currentTimeMillis();
        this.updatedAt=(long)0;
        this.title=title;
        this.MessageBody=MessageBody;
        this.senderName="";
        this.reciverName="";
        this.senderID=null;
        this.reciverID=null;

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
    public String getMessageBody(){
        return MessageBody;
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
    public void setMessageBody(String MessageBody){
        this.MessageBody= MessageBody;
    }

    public String getSenderName() {
        if(this.senderName!=null)
            return senderName;
        else
            return"";
    }
    public String getReciverName() {
        if(this.senderName!=null)
            return senderName;
        else
            return"";
    }
    public UUID getSenderID() {
        return senderID;
    }
    public UUID getReciverID() {
        return reciverID;
    }
    public void setReciverID(UUID reciverID) {
        this.reciverID = reciverID;
    }
    public void setSenderID(UUID senderID) {
        this.senderID = senderID;
    }
    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void SetSender(UUID senderID,String senderName){
        this.senderName=senderName;
        this.senderID=senderID;
    }
    public void SetReciver(UUID reciverID,String reciverName){
        this.reciverName = reciverName;
        this.reciverID = reciverID;
    }
    public String DiplayMessageInfo(){
        String one= "메세지 아이디: "+ID+" createdAt: "+ createdAt+ " updatedAt: "+updatedAt;
        String two="\nsenderName: "+senderName+" senderID : "+senderID;
        String three="\nreciverName: "+reciverName+" reciverID : "+reciverID;
        String four="\n제목 : "+title+" Message :"+MessageBody;
        return one+two+three+four;

    }


    public String gettitle() {
        return title;
    }

    public void setTtitle(String title) {
        this.title = title;
    }
}
