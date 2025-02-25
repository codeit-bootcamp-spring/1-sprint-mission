package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 5573544762128868590L;
    private UUID messageId = UUID.randomUUID();
    private String text;
    private Instant updatedAt = Instant.now();
    private UUID userId;
    private String userName;
    private UUID channelId;
    private String channelName;
    private Boolean isContainContent;

    SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
    @Override
    public String toString() {
        return "Message\n" +
                "messageId = " + messageId + '\n' +
                "text = " + text + '\n' +
                "channel = " + channelName + "\n" +
                "name = " + userName + '\n' +
                "updatedAt = " + (updatedAt) + '\n';
    }
}
