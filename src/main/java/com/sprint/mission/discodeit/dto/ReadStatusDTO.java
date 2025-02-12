package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record ReadStatusDTO() { //사용자가 채널에서 마지막으로 읽은 메시지의 상태 관리
    public static class createReadStatusRequestDTO {
        private UUID userId;
        private UUID channelId;
        private Long lastReadAt; //마지막으로 읽은 메시지 시간
    }

    public static class ReadStatusResponseDTO {
        private String id;
        private UUID userId;
        private UUID channelId;
        private Long lastReadAt;
    }

    public static class updateReadStatusRequestDTO {
        private Long lastReadAt;
    }
}