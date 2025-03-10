package com.sprint.mission.discodeit.dto;

import lombok.*;

import java.util.UUID;

@Setter @Getter
@Data @Builder
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private boolean online = false;
    private String profileImage;

    public UserDto(UUID id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}