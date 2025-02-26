package com.sprint.mission.discodeit.dto.request;

public record UserUpdateRequest(
    String username,
    String email,
    String password
) {

}
