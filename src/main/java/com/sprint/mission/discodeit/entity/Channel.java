package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID channeluuId;
    private final Instant createdAt;
    private Instant updatedAt;
    private String channelName;
    private String description;
    private final ChannelType type;


    public Channel(ChannelType type, String name,String description) {
        this.channeluuId = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.type = type;
        this.channelName = name;
        this.description = description;
    }



    public void update(String newName,String newDescription) {
        this.channelName = newName;
        this.description = newDescription;
        this.updatedAt = Instant.now();// 수정 시간을 갱신
    }

    @Override
    public String toString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Channel{\n" +
                "UUId : " + channeluuId +
                ", \nchannelName : " + channelName +
                ", \ncreatedAt : " + createdAt +
                ", \nupdatedAt : " + updatedAt +
                ", \ndescription : "+ description + "\n}";
    }

}
