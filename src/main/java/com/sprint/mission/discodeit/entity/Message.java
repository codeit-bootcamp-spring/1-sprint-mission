package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private final UUID channelId;
    private final UUID authorId;

    public Message(String content, UUID channelId, UUID authorId) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", channelId=" + channelId +
                ", authorId=" + authorId +
                '}';
    }

    public void update(String content) {
        boolean flag=false;
        if(content!=null&&!content.equals(this.content)) {
            this.content = content;
            flag=true;
        }
        if (flag){
            update();
        }
    }
}
