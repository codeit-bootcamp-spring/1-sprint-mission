package com.sprint.mission.discodeit.dto.user;

import lombok.Value;

@Value(staticConstructor = "of")
public class UserLoginDto {
    String name;
    String password;
}
