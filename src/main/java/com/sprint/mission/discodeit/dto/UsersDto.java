package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UsersDto {
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private boolean online = false;
    private String profileImage;
}