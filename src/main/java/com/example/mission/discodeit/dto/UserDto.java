package com.example.mission.discodeit.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String username;
    private String email;
    private UUID profileId;
    private Boolean online;
}
