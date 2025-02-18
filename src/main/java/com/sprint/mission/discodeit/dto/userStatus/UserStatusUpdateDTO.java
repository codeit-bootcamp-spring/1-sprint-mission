package com.sprint.mission.discodeit.dto.userStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusUpdateDTO {
    private Instant time;
}
