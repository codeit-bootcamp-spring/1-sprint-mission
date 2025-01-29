package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;

@Getter
public class MessageResDTO implements MessageDTO {
    private Long id;
    private String uuid;
    private String authorName;
    private String channelUuid;
    private String content;

    public MessageResDTO(Long id, Message msg) {
        this.id = id;
        this.uuid = msg.getId().toString();
        this.authorName = msg.getAuthor().getUserName().getName();
        this.channelUuid = msg.getChannel().getId().toString();
        this.content = msg.getContent();
    }

    @Override
    public String getMessageChannel() {
        return channelUuid;
    }

    @Override
    public String toString() {
        return "MessageResDTO{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", authorName='" + authorName + '\'' +
                ", channelUuid='" + channelUuid + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
