package com.sprint.mission.discodeit.dto.userStatus;

import com.sprint.mission.discodeit.entity.status.UserStatus;

public record UserStatusResponseDto (
    String id,
    boolean isOnline
){
    public static UserStatusResponseDto from(UserStatus userStatus){
        return new UserStatusResponseDto(
                userStatus.getId(),
                userStatus.isActive()
        );
    }
}
