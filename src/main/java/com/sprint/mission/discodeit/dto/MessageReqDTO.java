package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

@Getter
public class MessageReqDTO implements MessageDTO {
    private User author;
    private Channel channel;
    private String content;

    public MessageReqDTO(User author, Channel channel, String content) {
        this.author = author;
        this.channel = channel;
        this.content = content;
    }

    @Override
    public String getAuthorName() {
        return author.getUserName().getName();
    }

    @Override
    public String getMessageChannel() {     // UUID
        return channel.getId().toString();
    }
}
