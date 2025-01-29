package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Channel extends Common implements Serializable {
    private static final long serialVersionUID = 1L; //직렬화 버전
    //필드
    public enum ChannelType {
        PUBLIC,PRIVATE
    } //채널 타입
    private ChannelType channelType;
    private String channelName; // 채널 이름
    private String description; // 채널 설명

    public Channel(ChannelType channelType,String channelName, String description) {
        super();
        this.channelType = channelType;
        this.channelName = channelName;
        this.description = description;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelType=" + channelType +
                ", channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    // update 메소드
    public void updateDescription(String description) {
        this.description = description;
        setUpdatedAt(Instant.now().getEpochSecond());
    }

}
