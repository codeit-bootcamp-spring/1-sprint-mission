package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@Getter @Builder
public class UsersDto {
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private boolean online = false;
    private String profileImage;
}