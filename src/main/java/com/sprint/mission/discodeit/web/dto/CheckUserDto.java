package com.sprint.mission.discodeit.web.dto;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.domain.entity.UserStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CheckUserDto {
    private UUID id;
    private String userName;

    private String userEmail;

    private String loginId;

    private BinaryContent attachProfile;
    private boolean isOnline;

    public CheckUserDto(User user,Boolean isOnline) {
        this.id=user.getId();
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.loginId = user.getLoginId();
        this.attachProfile = user.getAttachProfile();
        this.isOnline = isOnline;
    }

    @Override
    public String toString() {
        return "CheckUserDto{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", loginId='" + loginId + '\'' +
                ", attachProfile=" + attachProfile +
                ", isOnline=" + isOnline +
                '}';
    }
}
