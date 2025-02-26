package com.sprint.mission.discodeit.dto.user.request;

public record CreateUserRequest(
    String username,
    String email,
    String password
) {

}
