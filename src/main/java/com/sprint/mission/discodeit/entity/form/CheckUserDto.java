package com.sprint.mission.discodeit.entity.form;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CheckUserDto {
    UserStatus status;
    private String userName;

    private String userEmail;

    private String loginId;

    private BinaryContent attachProfile;

    public CheckUserDto(User user) {
        this.status = user.getUserStatus();
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.loginId = user.getLoginId();
        this.attachProfile = user.getAttachProfile();
    }
}
