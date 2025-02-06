package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserStatusRequest {
    private UUID userId;
    private String status;
}
