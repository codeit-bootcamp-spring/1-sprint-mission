package com.sprint.mission.discodeit.entity;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable, Entity {
    private static final long serialVersionUID = 1L;

    // 일부 속성 세터 추가 가능성 있어 속성마다 @Getter 명시적으로 작성.
    @Getter private final long createdAt;
    @Getter private long updatedAt;
    @Getter private UUID id;
    @Getter private User author;
    @Getter private Channel channel;
    @Getter private String content;


    public Message(User User, Channel channel, String content){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.author = User;
        this.channel = channel;
        this.content = content;
    }


    //업데이트시간 변경
    public void setUpdatedAt(){this.updatedAt = System.currentTimeMillis();}
    //id 리턴

    public void setContent(String content){
        this.content = content;
        this.setUpdatedAt();
    }







}
