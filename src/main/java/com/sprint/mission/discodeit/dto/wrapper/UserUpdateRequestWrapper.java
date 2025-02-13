package com.sprint.mission.discodeit.dto.wrapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;

public record UserUpdateRequestWrapper(
        UserUpdateRequest userUpdateRequest,
        BinaryContentCreateRequest profileRequest
) {
}
