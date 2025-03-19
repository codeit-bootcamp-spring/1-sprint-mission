package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@Getter @Builder
public class UsersDto {
    private UUID id;
    private String name;
    private String email;
    private boolean online = false;
    private String profileImage;
}