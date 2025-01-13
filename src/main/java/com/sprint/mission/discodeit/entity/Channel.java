package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {

    private final UUID channeluuId;
    private final Long createdAt;
    private Long updatedAt;
    private String channelName;

    public Channel(String channelName) {
        this.channeluuId = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.channelName = channelName;
    }


    public UUID getuuId(){
        return channeluuId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getChannelName() {
        return channelName;
    }

    public void update(String channelName) {
        this.channelName = channelName;
        this.updatedAt = System.currentTimeMillis(); // 수정 시간을 갱신
    }

    @Override
    public String toString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Channel{\n" +
                "UUId=" + channeluuId +
                ", \nchannelName='" + channelName + '\'' +
                ", \ncreatedAt=" + sdf.format(new java.util.Date(createdAt)) +
                ", \nupdatedAt=" + sdf.format(new java.util.Date(updatedAt)) +
                '}';
    }

}
