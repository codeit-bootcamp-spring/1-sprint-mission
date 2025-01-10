package com.spirnt.mission.discodeit.entity;

public class Message extends BaseEntity{
    private String content;
    private User sender;
    private User recipient;
    private Channel channel;


    public Message(String content, User sender, Channel channel) {
        setContent(content);
        setSender(sender);
        setChannel(channel);
    }

    public Message(String content, User sender, User recipient) {
        setContent(content);
        setSender(sender);
        setRecipient(recipient);
    }

    public void setContent(String content){ this.content = content; }
    public void setSender(User sender){ this.sender = sender; }
    public void setRecipient(User recipient){ this.recipient = recipient; }
    public void setChannel(Channel channel){ this.channel = channel; }

    public String getContent(){ return content; }
    public User getSender(){
        return sender;
    }
    public User getRecipient() {return recipient; }
    public Channel getChannel(){
        return channel;
    }

//    public void updateContent(String content){
//        this.content = content;
//        update();
//    }
}
