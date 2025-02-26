package com.spirnt.mission.discodeit.dto.user;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserCreateResponse {
    private UUID userId;
    private String name;
    private String email;
    private UUID profileImage;

    public UserCreateResponse(UUID userId, String name, String email, UUID profileImage) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
    }
}
