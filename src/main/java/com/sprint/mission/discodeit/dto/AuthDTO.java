package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record AuthDTO() {
    public static class LoginRequestDTO {
        private String userName;
        private String password;
    }

    public static class LoginResponseDTO {
        private UUID userId;
        private String userName;
        private String userEmail;
        private boolean online;
    }
}