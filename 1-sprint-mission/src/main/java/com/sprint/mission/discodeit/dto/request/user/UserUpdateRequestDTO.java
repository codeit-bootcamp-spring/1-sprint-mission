package com.sprint.mission.discodeit.dto.request.user;

public record UserUpdateRequestDTO(
        UserUpdateDTO userUpdateDTO,
        UserProfileImageDTO userProfileImageDTO
) {
}
