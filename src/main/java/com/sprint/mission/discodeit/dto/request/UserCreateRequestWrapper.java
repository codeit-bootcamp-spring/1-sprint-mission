package com.sprint.mission.discodeit.dto.request;

import java.util.Optional;

public record UserCreateRequestWrapper(
        UserCreateRequest userCreateRequest,
        Optional<BinaryContentCreateRequest> profileCreateRequest
) {
}
