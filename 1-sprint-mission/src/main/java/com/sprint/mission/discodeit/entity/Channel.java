package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Channel extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID channelUuid;
    private final Long createdAt;
    private Long updatedAt;
    private String channelTitle;
    private final String userId;

    public Channel(String channelTitle, String userId){
        channelUuid = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.channelTitle = channelTitle;
        this.userId = userId;
    }

// Get 메소드
    public String getChannelTitle() {
        return channelTitle;
    }

    public String getUserId() {
        return userId;
    }


    public String getChannelUuid() {
        return channelUuid.toString();
    }

//update 메소드

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
        updateUpdatedAt();
    }

}
