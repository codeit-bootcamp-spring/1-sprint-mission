package com.sprint.mission.discodeit.entity.user;

public record UserLoginRequest(
        String userName,
        String userPassword
) {
}
