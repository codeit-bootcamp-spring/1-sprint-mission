package com.sprint.mission.discodeit.dto.user.response;


public record FindUserResponse(
        String name,
        String nickname,
        String email,
        byte[] profileImage,
        boolean isOnline
) {
}
