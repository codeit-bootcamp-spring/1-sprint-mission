package com.sprint.mission.discodeit.dto.request.users.create;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequest;

import java.util.Optional;

public record UserCreateRequestWrapper(
        UserCreateRequest userCreateRequest,
        Optional<BinaryContentCreateRequest> profileCreateRequest
) {
}
