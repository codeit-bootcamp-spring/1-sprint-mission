package com.sprint.mission.discodeit.entity;

import java.io.Serial;

public class Channel extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 2L;
    private String name; //채널명
    private String topic; //채널의 목적 규칙에 대한 설명
    private ChannelType type; //채널 타입

    public Channel(String name, String topic, ChannelType type) {
        super();
        this.name = name;
        this.topic = topic;
        this.type = type;
    }

    public void setChannelInfo(String name, String topic, ChannelType type){
        this.name = name;
        this.topic = topic;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public ChannelType getType() {
        return type;
    }

    public void update(String name, String topic, ChannelType type) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (topic != null && !topic.trim().isEmpty()) {
            this.topic = topic;
        }
        if (type != null) {
            this.type = type;
        }
        updateTimeStamp();
    }
}
