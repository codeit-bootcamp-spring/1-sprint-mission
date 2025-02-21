package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record UserRegisterRequest(
    String name,
    String email,
    String phoneNumber,
    BinaryContent image
) {

}
