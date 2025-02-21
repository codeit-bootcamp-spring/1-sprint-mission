package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //
    private UUID channelId;
    private UUID authorId;
    private String messageText;
    private List<UUID> attachmentIds;

    public Message(UUID channelId, UUID authorId, String messageText, List<UUID> attachmentIds){
        // 공백일 수 있다.
        if(messageText == null){
            throw new IllegalArgumentException("messageText 은 null 일 수 없습니다.");
        }
        this.channelId = channelId;
        this.authorId = authorId;
        this.messageText = messageText;
        this.attachmentIds = attachmentIds;
    }

    public void updateMessageText(String messageText){
        if(messageText == null){
            throw new IllegalArgumentException("channelName 은 null 일 수 없습니다.");
        }
        this.messageText = messageText;
        this.refreshUpdateAt();
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
