package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Getter
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID UuId;
    private final Channel destinationCh;
    private final Instant createdAt;
    private Instant updatedAt;
    private String content;
    private final User SendUser;


    public Message(User SendUser, Channel destinationCh, String content) {
        this.UuId = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.content = content;
        this.SendUser = SendUser;
        this.destinationCh = destinationCh;
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = Instant.now(); // 수정 시간을 갱신
    }

    @Override
    public String toString() {
        return "Message{\n" +
                "UUId=" + UuId +
                ", \ndestinationChannel : " + destinationCh.getChannelName() +
                ", \ncreatedAt : " + createdAt +
                ", \nupdatedAt : " + updatedAt +
                ", \ncontent : " + content + '\'' +
                ", \nsendUser : " + SendUser.getName() + "\n}";
    }

}
