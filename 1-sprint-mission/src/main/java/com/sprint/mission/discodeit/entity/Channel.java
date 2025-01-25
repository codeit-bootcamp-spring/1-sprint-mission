package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel extends BaseEntity{

    private final UUID channelUuid;
    private final Long createdAt;
    private Long updatedAt;
    private String channelTitle;
    private String userId;

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


    public UUID getChannelUuid() {
        return channelUuid;
    }

//update 메소드
    public void updateUserId(String userId) {
        this.userId = userId;
        updateUpdatedAt();
    }

    public void updateUserName(String channelTitle) {
        this.channelTitle = channelTitle;
        updateUpdatedAt();
    }

}
