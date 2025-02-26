package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

//요청
public record UserCreateDto(
        String username,
        String email,
        String password,
        String phoneNumber,
        String address
) { }
