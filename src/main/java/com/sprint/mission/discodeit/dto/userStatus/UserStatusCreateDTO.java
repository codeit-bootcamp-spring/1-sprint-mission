package com.sprint.mission.discodeit.dto.userStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusCreateDTO {
    private UUID userid;
}
