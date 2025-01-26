package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity{
            private String channelName;

            public Channel(String channelName, User owner) {
                super();
                this.channelName = channelName;
            }

            public String getChannelName() {
                return channelName;
            }

            public void setChannelName(String channelName) {
                this.channelName = channelName;
            }

            @Override
            public String toString() {
                return "Channel{" +
                        "channelId=" + getId() +
                        ", channelName='" + channelName + '\'' +
                        ", createdAt=" + getCreatedAt() +
                        ", updatedAt=" + getUpdatedAt() +
                        '}';
            }
        }