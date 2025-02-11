package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

public class ChannelDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PrivateChannelDTO {
        private List<UUID> userIds;  // 참여할 유저들의 ID
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PublicChannelDTO {
        private String name;
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateChannelDTO {
        private String newName;
        private String newDescription;
    }
}
