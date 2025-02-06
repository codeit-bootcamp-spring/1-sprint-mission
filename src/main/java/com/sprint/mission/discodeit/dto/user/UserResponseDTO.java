package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDTO {
    private String name;
    private String email;
    private boolean isOnline;
}
