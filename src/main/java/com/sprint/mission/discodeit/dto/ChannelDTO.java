package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record ChannelDTO() {
    public static class createChannelRequestDTO {
        private String channelName;
    }
    public static class ChannelResponseDTO {
        private UUID channelId;
        private String channelName;
        private String channelType;
        private Long latestMessageTimestamp;
        private List<String> participantUserIds;
    }

    public static class updateChannelRequestDTO {
        private String channelName;
    }
    public static class deleteChannelRequestDTO {
        private UUID channelId;
    }
}