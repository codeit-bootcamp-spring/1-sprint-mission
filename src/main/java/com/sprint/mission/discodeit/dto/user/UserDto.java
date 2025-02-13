package com.sprint.mission.discodeit.dto.user;

import lombok.Value;

@Value(staticConstructor = "of")
public class UserCreateDto {
    String email;
    String password;
    String name;
}
