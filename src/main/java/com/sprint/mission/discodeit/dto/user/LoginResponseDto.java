package com.sprint.mission.discodeit.dto.user;

public record LoginResponseDto(
    String id,
    String username,
    String email,
    String profileId
) {
}
