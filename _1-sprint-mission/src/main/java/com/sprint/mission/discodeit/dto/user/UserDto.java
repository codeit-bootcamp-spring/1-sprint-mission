package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record UserDto(
        UUID userId,
        String username,
        String email,
        String password,
        String phoneNumber,
        String address,
        BinaryContent profileImage
) { }
