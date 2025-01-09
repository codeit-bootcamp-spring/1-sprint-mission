package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Message extends BaseEntity{
    private String content; //메시지 내용
    private User author; //작성자
    private List<User> mentions; //멘션된 사용자 리스트
    private Channel channel; //메시지가 속한 채널

    public Message(String content, User author, Channel channel) {
        super();
        this.content = content;
        this.author = author;
        this.channel = channel;
        this.mentions = new ArrayList<>();
    }

    public String getContent() {
        return content;
    }

    public List<User> getMentions() {
        return mentions;
    }

    public User getAuthor() {
        return author;
    }


    public Channel getChannel() {
        return channel;
    }

    public void update(String content) {
        if(content != null && !content.trim().isEmpty()) {
            this.content = content;
            updateTimeStamp();
        }
    }

}

