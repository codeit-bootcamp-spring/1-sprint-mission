package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Message extends Common implements Serializable {
    private static final long serialVersionUID = 1L; //직렬화 버전
    //필드
    private String content; //메세지내용
    private UUID channelId; // 채널 id
    private UUID authorId; // 작성자 id

    //생성자

    public Message(String content, UUID channelId, UUID authorId) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }

    //Getter
    public String getContent() {
        return content;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    @Override
    public String toString() {
        return  "content='" + content + '\'' +
                ", channelId=" + channelId +
                ", authorId=" + authorId;
    }

    // update 메소드
    public void updateContent(String newContent) {
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            setUpdatedAt(Instant.now().getEpochSecond());
        }
    }
}
