package com.sprint.mission.discodeit.dto.user;


import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.security.Role;
import lombok.Getter;

import java.util.UUID;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private UUID id;
    private String username;
    private String email;
    private BinaryContentDto profile;
    private boolean isOnline;
    private Role role;
}
