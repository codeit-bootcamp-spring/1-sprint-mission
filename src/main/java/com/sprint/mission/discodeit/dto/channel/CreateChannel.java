package com.sprint.mission.discodeit.dto.channel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import com.sprint.mission.discodeit.util.type.ChannelFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

public class CreateChannel {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PrivateRequest {
        private List<UUID> joinUser;
        private ChannelFormat channelFormat;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PublicRequest {
        private String name;
        private String description;
        ChannelFormat channelFormat;
        private List<UUID> joinUser;
    }
}
