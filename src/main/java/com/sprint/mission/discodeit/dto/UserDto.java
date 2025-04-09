package com.sprint.mission.discodeit.dto;

import lombok.*;

import java.util.UUID;

@Setter @Getter
@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private String password;
    private boolean online = false;
    private String profileImage;
    private BinaryContentDto profile;

    public UserDto(UUID id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.username = name;
        this.email = email;
        this.password = password;
    }
}