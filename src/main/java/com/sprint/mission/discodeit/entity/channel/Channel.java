package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;

@Getter
public class Channel extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 12L;
    private String name; //채널명
    private String topic; //채널의 목적 규칙에 대한 설명
    private ChannelType type; //채널 타입

    @Builder
    public Channel(String name, String topic, ChannelType type) {
        super();
        this.name = name;
        this.topic = topic;
        this.type = type;
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
