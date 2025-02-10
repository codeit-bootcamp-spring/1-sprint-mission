package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.UserStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserServiceFindDTO {
    private UUID id;
    private String username;
    private String email;
    private UserStatusType type;
}
