package com.sprint.mission.discodeit.dto.user;


import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;;
    private UUID profileId;
    private boolean isOnline;

    public UserDto(UUID id, String name, String email, Instant createdAt, Instant updatedAt,UUID profileId ,boolean isOnline) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.profileId = profileId;
        this.isOnline = isOnline;
    }
}
