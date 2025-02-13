package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserUpdateRequest(
        String newUsername,
        String newEmail,
        String newPhoneNumber,
        String newPassword
) {}
