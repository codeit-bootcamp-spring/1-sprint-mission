package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends Common{
    //필드
    private String content; //메세지내용
    private User sender; // 보내는사람
    private Channel channel; //메시지가 속한 채널
    private String emotions; //이모티콘

    //생성자
    public Message(String content, User sender, Channel channel, String emotions) {
        super();
        this.content = content;
        this.sender = sender;
        this.channel = channel;
        this.emotions = emotions;
    }
    //Getter
    public String getContent() {
        return content;
    }
    public User getSender() {
        return sender;
    }
    public Channel getChannel() {
        return channel;
    }
    public String getEmotions() {
        return emotions;
    }

    @Override
    public String toString() {
        return "메시지 내용='" + content + '\'' +
                ", 보내는 사람=" + sender.getUsername() +
                ", 채널=" + channel.getChannelName() +
                ", 이모티콘='" + emotions + '\''+
                ", 생성='"+ getFormattedCreatedAt();
    }
    public String toStringUpdate() {
        return "메시지 내용='" + content + '\'' +
                ", 보내는 사람=" + sender.getUsername() +
                ", 채널=" + channel.getChannelName() +
                ", 이모티콘='" + emotions + '\''+
                ", 수정='"+ getFormattedUpdatedAt();
    }

    // update 메소드
    public void updateContent(String content) {
        this.content = content; // 메시지 내용 갱신
        setUpdatedAt(System.currentTimeMillis()); // 수정 시간 갱신
    }
    public void updateSender(User sender) {
        this.sender = sender; // 메시지 보낸 사용자 갱신
        setUpdatedAt(System.currentTimeMillis()); // 수정 시간 갱신
    }
    public void updateChannel(Channel channel) {
        this.channel = channel; // 메시지가 속한 채널 갱신
        setUpdatedAt(System.currentTimeMillis()); // 수정 시간 갱신
    }
    public void updateEmoji(String emotions) {
        this.emotions = emotions; // 메시지의 이모티콘 갱신
        setUpdatedAt(System.currentTimeMillis()); // 수정 시간 갱신
    }
}
