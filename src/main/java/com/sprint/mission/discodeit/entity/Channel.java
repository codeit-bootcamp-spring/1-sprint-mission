package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
@Setter

public class Channel implements Serializable  {

    private static final Long serialVersionUID = 1L;
    private final UUID id ;
    private final Instant createdAt;
    private Instant updatedAt;

    private String channelName;
    private ChannelType type;

    public Channel(ChannelCreateDTO channelCreateDTO, ChannelType type){
        this.id = UUID.randomUUID();
        this.createdAt =  Instant.now();
        this.updatedAt=createdAt;

        this.channelName = channelName;
        this.type = ChannelType.valueOf("PRivate");
    }


    //update
    public void updateName(String name){
        this.channelName=name;
        this.updateUpdatedAt();
    }
    public void updateUpdatedAt(){
        this.updatedAt=Instant.now();  //업데이트 시간
    }


}
