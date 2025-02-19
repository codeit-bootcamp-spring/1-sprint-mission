package com.sprint.mission.discodeit.dto.userStatus;

import com.sprint.mission.discodeit.entity.UserStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusUpdateDTO {
    private UUID id;
    private UserStatusType type;
}
