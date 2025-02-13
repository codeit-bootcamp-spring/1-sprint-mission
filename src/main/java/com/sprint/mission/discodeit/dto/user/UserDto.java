package com.sprint.mission.discodeit.dto.user;

import lombok.Value;

@Value(staticConstructor = "of")
public class UserDto {
    String email;
    String password;
    String name;
}
