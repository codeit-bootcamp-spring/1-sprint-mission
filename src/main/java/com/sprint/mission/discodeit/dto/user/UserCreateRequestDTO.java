package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.UserStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDTO {
    private String name;
    private String email;
    private String password;
    private byte[] profileImage;
    private String status;
}
