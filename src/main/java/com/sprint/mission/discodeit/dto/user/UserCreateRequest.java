package com.sprint.mission.discodeit.dto.user;

public record UserCreateRequest(
    String username,
    String email,
    String password
) {

}
