package com.sprint.mission.discodeit.dto.wrapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;

public record UserCreateRequestWrapper(
    UserCreateRequest userCreateRequest,
    BinaryContentCreateRequest profileCreateRequest
) {

}
