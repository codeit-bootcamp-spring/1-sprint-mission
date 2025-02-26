package com.sprint.mission.discodeit.dto.request.user;

public record UserCreateRequestDTO(
         UserCreateDTO userCreateDTO,
         UserProfileImageDTO userProfileImageDTO
) {
}
