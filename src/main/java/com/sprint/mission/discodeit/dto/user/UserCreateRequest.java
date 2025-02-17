package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.util.UserRegex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserCreateRequest(
        String username,
        String email,
        String phoneNumber,
        String password
) {
}
