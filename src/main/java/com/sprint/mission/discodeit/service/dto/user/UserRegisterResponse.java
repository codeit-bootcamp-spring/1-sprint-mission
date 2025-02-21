package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;

public record UserRegisterResponse(
    UUID id,
    String name,
    String email,
    String phoneNumber,
    UserStatus userStatus,
    ReadStatus readStatus,
    BinaryContent image
) {

    public static UserRegisterResponse from(User user) {
        return new UserRegisterResponse(
            user.id(),
            user.name(),
            user.email(),
            user.phoneNumber(),
            user.userStatus(),
            user.readStatus(),
            user.image()
        );
    }
}
