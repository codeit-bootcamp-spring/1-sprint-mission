package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private User sender;
    private User recipient;
    private Channel channel;


    public Message(String content, User sender, Channel channel) {
        super();
        this.content = content;
        this.sender = sender;
        this.channel = channel;
    }

    public Message(String content, User sender, User recipient) {
        super();
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
    }
}
