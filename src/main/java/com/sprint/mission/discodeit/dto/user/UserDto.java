package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;

import java.util.UUID;

public record UserDto(
        UUID id,
        String email,
        String name,
        String nickname,
        String phoneNumber,
        BinaryContentDto profile,
        boolean isOnline
) {
}