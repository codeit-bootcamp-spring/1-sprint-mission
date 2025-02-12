package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Message extends BaseEntity {
    private String content;
    private User user;
    private Channel channel;


    public Message(User user, Channel channel, String content) {
        super();
        this.user = user;
        this.channel = channel;
        this.content = content;
    }

    public Message(Message message) {
        super(); // BaseEntity의 필드 초기화
        this.user = message.getUser();
        this.channel = message.getChannel();
        this.content = message.getContent();
    }


}