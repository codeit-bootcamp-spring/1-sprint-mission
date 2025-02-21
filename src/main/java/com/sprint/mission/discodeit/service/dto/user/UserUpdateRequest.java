package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;

public record UserUpdateRequest (
    UUID id,
    String name,
    String email,
    String phoneNumber,
    UserStatus userStatus,
    ReadStatus readStatus,
    BinaryContent image
) {

}
