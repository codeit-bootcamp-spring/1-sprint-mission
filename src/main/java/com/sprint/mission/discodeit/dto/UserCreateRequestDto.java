package com.sprint.mission.discodeit.dto;

public record UserCreateRequestDto(
        String name,
        String email,
        String password
) {
}
