package com.sprint.mission.discodeit.dto.user;


import lombok.Getter;

@Getter
public class UserResponseDto {
    private String name;
    private String email;
    private boolean isOnline;

    public UserResponseDto(String name, String email, boolean isOnline) {
        this.name = name;
        this.email = email;
        this.isOnline = isOnline;
    }
}
