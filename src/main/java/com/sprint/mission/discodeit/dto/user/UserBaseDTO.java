package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBaseDTO {
    private String userName;
    private String userEmail;
    public UserBaseDTO (String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
