package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Message implements Serializable {

    private static final Long serialVersionUID = 1L;
    private UUID id ;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private UUID channelId;
    private String content;
    private List<BinaryContent> binaryContentList; //파일 용량/개수 제한을 둬야함



    public Message(MessageCreateDTO messageCreateDTO){
        this.id = UUID.randomUUID();
        this.createdAt =  Instant.now();
        this.updatedAt=createdAt;

        this.userId = messageCreateDTO.userId();
        this.channelId=messageCreateDTO.channelId();
        this.content = messageCreateDTO.content();
        this.binaryContentList=messageCreateDTO.binaryContentList();
    }

    //update
    public void updateUpdatedAt(){
        this.updatedAt=Instant.now(); //업데이트 시간
    }
    public void updateContent(MessageUpdateDTO messageUpdateDTO){
        this.content = messageUpdateDTO.content();
        this.binaryContentList=messageUpdateDTO.binaryContentList();
        updateUpdatedAt();;
    }


    //delete related Domain
    public void deleteBinaryContentList(){
        this.binaryContentList=null;
    }
}
