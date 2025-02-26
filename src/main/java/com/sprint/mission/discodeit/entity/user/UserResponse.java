package com.sprint.mission.discodeit.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter @Getter
@AllArgsConstructor
public class UserResponse {
    private String userName;
    private String userEmail;
    private String userStatus;
    private String profileImg;

    public UserResponse(String userName, String userEmail, String userStatus) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userStatus = userStatus;
    }
}
