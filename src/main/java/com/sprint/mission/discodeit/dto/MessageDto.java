package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;
import lombok.Value;

@Getter
public class MessageDto {

    private User writer;
    private String content;
    private Channel channel;

    private MessageDto(User writer, String content, Channel channel) {
        this.writer = writer;
        this.content = content;
        this.channel = channel;
    }

    public static MessageDto of(User writer, String content, Channel channel) {
        return new MessageDto(writer, content, channel);
    }
}
