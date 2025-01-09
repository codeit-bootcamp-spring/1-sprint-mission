package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity{
    private Channel channel;
    private String messageText;
    public Message(Channel channel, String messageText){
        //this.user = user; // 채널을 중심으로 동작해보려고
        this.channel = channel;
        this.messageText = messageText;
    }
    public String getMessageText(){
        return messageText;
    }
    public void setMessageText(String messageText){
        this.messageText = messageText;
    }
    public Channel getChannel(){
        return channel;
    }
}
