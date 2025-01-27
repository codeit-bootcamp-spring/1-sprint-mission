package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
public class Message extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final User writer;
    private final Channel channel;
    private String content;

    public Message(Channel channel, User writer, String content) {
        super();
        this.channel = channel;
        this.writer = writer;
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
        update();
    }


}
