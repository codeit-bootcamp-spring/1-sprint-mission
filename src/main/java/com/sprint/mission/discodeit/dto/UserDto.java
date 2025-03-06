package com.sprint.mission.discodeit.dto;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String password;

}