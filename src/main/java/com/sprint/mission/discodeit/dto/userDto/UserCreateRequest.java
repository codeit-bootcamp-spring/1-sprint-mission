package com.sprint.mission.discodeit.dto.userDto;

public record UserCreateRequest(
    String username,
    String email,
    String password
) {

}
