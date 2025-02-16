package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Gender;

import java.util.UUID;

public record UpdateUserDto(
        UUID id,
        String name,
        int age,
        Gender gender,
        byte[] profileImageUrl
) {
}
