package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Message implements Serializable, Entity {
    private static final long serialVersionUID = 1L;

    private final Instant createdAt;
    private Instant updatedAt;
    private UUID id;
    private User author;
    private Channel channel;
    private String content;


    public Message(User User, Channel channel, String content){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.author = User;
        this.channel = channel;
        this.content = content;
    }


    //업데이트시간 변경
    public void setUpdatedAt(){this.updatedAt = Instant.now();}
    //id 리턴

    public void setContent(String content){
        this.content = content;
        this.setUpdatedAt();
    }







}
