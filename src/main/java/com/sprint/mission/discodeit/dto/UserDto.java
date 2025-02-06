package com.sprint.mission.discodeit.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class UserDto {
    String loginId;
    String password;
    String name;
}
