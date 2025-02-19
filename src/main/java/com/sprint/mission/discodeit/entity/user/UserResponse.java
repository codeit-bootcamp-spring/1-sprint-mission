package com.sprint.mission.discodeit.entity.user;

import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter @Getter
@AllArgsConstructor
public class UserResponse {
    private String userName;
    private String userEmail;
    private UserStatus userStatus;
}
