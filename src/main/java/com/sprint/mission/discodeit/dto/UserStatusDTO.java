package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserStatusDTO() { //사용자의 온라인/오프라인 상태를 관리
    public static class creatUserStatusRequestDTO {
        private UUID userId;
        private Long lastActiveAt;
    }
    public static class userStatusResponseDTO {
        private String id;
        private UUID userId;
        private Long lastActiveAt;
        private boolean isOnline;
    }
    public static class updateUserStatusRequestDTO {
        private Long lastActiveAt;
    }
}