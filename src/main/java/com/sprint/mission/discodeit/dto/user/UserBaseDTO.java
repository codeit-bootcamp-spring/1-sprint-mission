package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserBaseDTO {
    private String userName;
    private String userEmail;
    public UserBaseDTO (UUID id, String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
