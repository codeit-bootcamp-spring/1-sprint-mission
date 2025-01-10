package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.constant.ChannelConstant;
import com.sprint.mission.discodeit.exception.ChannelValidationException;
import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;


@Getter @Setter
public class Channel {
    private final String UUID;
    private final String ServerUUID;
    private final String CategoryUUID;
    private String channelName;
    private int maxNumberOfPeople;
    private String tag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isPrivate;
    private final ChannelBehavior behavior;

    private Channel(ChannelBuilder builder) {
        this.UUID = UuidGenerator.generateUUID();
        this.ServerUUID = builder.serverUUID;
        this.CategoryUUID = builder.categoryUUID;
        this.channelName = builder.channelName;
        this.maxNumberOfPeople = builder.maxNumberOfPeople;
        this.isPrivate = builder.isPrivate;
        this.tag = Optional.ofNullable(builder.tag).orElse(ChannelConstant.BASIC_TAG);
        this.behavior = builder.behavior;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static class ChannelBuilder {
        private final String serverUUID;
        private final String categoryUUID;
        private final String channelName;
        private int maxNumberOfPeople = 50;
        private Boolean isPrivate = false;
        private String tag;
        private ChannelBehavior behavior;

        public ChannelBuilder(String serverUUID, String categoryUUID, String channelName, ChannelBehavior channelBehavior) throws ChannelValidationException {
            if(serverUUID == null || categoryUUID == null || channelName == null || channelBehavior == null){
                throw new ChannelValidationException();
            }
            this.serverUUID = serverUUID;
            this.categoryUUID = categoryUUID;
            this.channelName = channelName;
            this.behavior = channelBehavior;
        }

        public ChannelBuilder maxNumberOfPeople(int maxNumberOfPeople) {
            this.maxNumberOfPeople = maxNumberOfPeople;
            return this;
        }

        public ChannelBuilder isPrivate(Boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public ChannelBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Channel build() {
            return new Channel(this);
        }
    }
    public void updatePrivate(Boolean aPrivate, Channel channel) {
        if(aPrivate) behavior.setChannelPublic(channel);
        else behavior.setChannelPrivate(channel);
    }

    @Override
    public String toString() {
        return "Channel{" +
            "UUID='" + UUID + '\'' +
            ", ServerUUID='" + ServerUUID + '\'' +
            ", CategoryUUID='" + CategoryUUID + '\'' +
            ", channelName='" + channelName + '\'' +
            ", maxNumberOfPeople=" + maxNumberOfPeople +
            ", tag='" + tag + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", isPrivate=" + isPrivate +
            ", behavior=" + behavior +
            '}';
    }
}
