package com.sprint.mission.discodeit.dto.form;

import com.sprint.mission.discodeit.dto.entity.BinaryContent;
import com.sprint.mission.discodeit.dto.entity.User;
import com.sprint.mission.discodeit.dto.entity.UserStatus;
import lombok.Getter;

@Getter
public class CheckUserDto {
    UserStatus status;
    private String userName;

    private String userEmail;

    private String loginId;

    private BinaryContent attachProfile;

    public CheckUserDto(User user) {
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.loginId = user.getLoginId();
        this.attachProfile = user.getAttachProfile();
    }
}
