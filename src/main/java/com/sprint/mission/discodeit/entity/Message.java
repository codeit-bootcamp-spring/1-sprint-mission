package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class Message extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 추가할 수 있는 것 : Channel 도메인에 채널에 소속(참가)된 사용자가 추가된 이후,
        // 채널에 소속된 사용자 중 "누가" message를 보냈는지 지정하기
    private UUID channelId;
    private UUID authorId;
    private String messageText;

    public Message(UUID channelId, UUID authorId, String messageText){
        // 공백일 수 있다.
        if(messageText == null){
            throw new IllegalArgumentException("messageText 은 null 일 수 없습니다.");
        }
        this.channelId = channelId;
        this.authorId = authorId;
        this.messageText = messageText;
    }

    // setter 이름 변경하기
    public void setMessageText(String messageText){
        if(messageText == null){
            throw new IllegalArgumentException("channelName 은 null 일 수 없습니다.");
        }
        this.messageText = messageText;
    }

    @Override
    public String toString(){
        return "\n"
                + "authorId : " + getAuthorId()
                + "\n"
                + "channelId : " + getChannelId()
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
