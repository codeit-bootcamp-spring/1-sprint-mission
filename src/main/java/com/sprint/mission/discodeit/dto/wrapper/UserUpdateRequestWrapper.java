package com.sprint.mission.discodeit.dto.wrapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;

import java.util.Optional;

public record UserUpdateRequestWrapper(
        UserUpdateRequest userUpdateRequest,
        BinaryContentCreateRequest profileRequest
) {
}
