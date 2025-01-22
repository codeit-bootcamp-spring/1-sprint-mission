package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity{
    private Channel channel;
    private String messageText;
    public Message(Channel channel, String messageText){
        if(channel == null){
            throw new IllegalArgumentException("Channel 은 null 일 수 없습니다.");
        }
        // 공백일 수 있다.
        if(messageText == null){
            throw new IllegalArgumentException("channelName 은 null 일 수 없습니다.");
        }
        this.channel = channel;
        this.messageText = messageText;
    }
    public String getMessageText(){
        return messageText;
    }
    public void setMessageText(String messageText){
        if(messageText == null){
            throw new IllegalArgumentException("channelName 은 null 일 수 없습니다.");
        }
        this.messageText = messageText;
    }
    public Channel getChannel(){
        return channel;
    }

    @Override
    public String toString(){
        return "\n"
                + "User : " + channel.getUser().getNickname()
                + "\n"
                + "Channel : " + channel.getChannelName()
                + "\n"
                + "^^^^^^^^^^^^^^^^^^^^^^^^^message^^^^^^^^^^^^^^^^^^^^^^^^^^^"
                + "\n"
                + "Message txt : " + getMessageText()
                + "\n"
                + "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv"
                + "\n"
                + "created at : " + getCreatedAt()
                + "\n"
                + "updated at : " + getUpdatedAt()
                + "\n";
    }
}
