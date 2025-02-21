package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserUpdateRequest(
        UUID userId,
        String username,
        String email,
        String phoneNumber,
        String password,
        byte[] profileImage // 선택적으로 프로필 이미지
) {}
