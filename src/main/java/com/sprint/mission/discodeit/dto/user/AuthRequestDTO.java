package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class AuthRequestDTO {
    private final String name;
    private final String password;
}
