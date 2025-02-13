package com.sprint.mission.discodeit.dto.wrapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;

import java.util.Optional;

public record UserCreateRequestWrapper(
        UserCreateRequest userCreateRequest,
        BinaryContentCreateRequest ProfileCreateRequest
) {
}
