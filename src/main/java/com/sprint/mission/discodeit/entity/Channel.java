package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Channel extends BaseEntity{
            private String channelName;

            public Channel(String channelName, User owner) {
                super();
                this.channelName = channelName;
            }
            public Channel (Channel channel) {
                super();
            }
        }