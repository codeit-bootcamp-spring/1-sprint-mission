package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserStatusRequest {
    private UUID userId;
    private String status;

    public UserStatusRequest(UUID userId, String status) {
        this.userId = userId;
        this.status = status;
    }
}
