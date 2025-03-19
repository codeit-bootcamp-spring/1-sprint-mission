package com.sprint.mission.discodeit.dto.request;

public record UserCreateRequest(
    String username,
    String email,
    String password
) {

}
