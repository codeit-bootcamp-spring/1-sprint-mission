package com.sprint.mission.discodeit.dto.request.users.update;

import com.sprint.mission.discodeit.dto.request.users.BinaryContentCreateRequest;

import java.util.Optional;

public record UserUpdateRequestWrapper(
        UserUpdateRequest userUpdateRequest,
        Optional<BinaryContentCreateRequest> profileCreateRequest

) {
}
