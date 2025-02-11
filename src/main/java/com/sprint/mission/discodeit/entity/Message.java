package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Message extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final User writer;
    private final Channel channel;
    private String content;

    @Builder
    public Message(Channel channel, User writer, String content) {
        super();
        this.channel = channel;
        this.writer = writer;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
        update();
    }


}
