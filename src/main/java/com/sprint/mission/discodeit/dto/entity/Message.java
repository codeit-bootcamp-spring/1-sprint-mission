package com.sprint.mission.discodeit.dto.entity;


import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity implements Serializable {
    private String content;
    private UUID senderId;
    private UUID channelId;

    private List<MultipartFile> messageFiles;

    public Message(String content, UUID senderId, UUID channelId) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    public void updateMessage(String content) {
        this.content = content;
        setUpdatedAt();
    }
    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                '}';
    }
}
