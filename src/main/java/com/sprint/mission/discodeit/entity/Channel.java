package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;


@Getter
@Setter

public class Channel implements Serializable  {

    private static final Long serialVersionUID = 1L;
    private final UUID id ;
    private String channelName;
    private final Long createdAt;
    private Long updatedAt;

    public Channel(String channelName){
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.createdAt =  System.currentTimeMillis() / 1000; // 초 단위로 변환
    }

    //update
    public void updateName(String name){
        this.channelName=name;
        this.updateUpdatedAt();
    }
    public void updateUpdatedAt(){
        this.updatedAt=System.currentTimeMillis() /1000; //업데이트 시간
    }


}
