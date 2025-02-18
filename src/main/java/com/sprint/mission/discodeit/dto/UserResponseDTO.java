package com.sprint.mission.discodeit.dto;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;

    public UserResponseDTO(UUID id, String username, String email, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}