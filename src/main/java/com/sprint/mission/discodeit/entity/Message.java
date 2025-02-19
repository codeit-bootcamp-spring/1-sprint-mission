package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.message.MessageDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private UUID authorId;
    private  UUID channelId;

    public Message(MessageDTO dto) {
        super();
        this.content = dto.getContent();
        this.authorId = dto.getUserId();
        this.channelId = dto.getChannelId();
    }
    public void updateMessage(String content) {
        if(content == null || content.equals(this.content)) return;
        this.content = content;
        updateTime(Instant.now());
    }
    public void update(MessageDTO dto) {
        if(dto.getContent() != null && !dto.getContent().equals(this.content)) {
            this.content = dto.getContent();

        }
    }
}
