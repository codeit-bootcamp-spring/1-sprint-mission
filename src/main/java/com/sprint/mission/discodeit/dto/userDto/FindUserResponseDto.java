package com.sprint.mission.discodeit.dto.userDto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FindUserResponseDto {
    UUID id;
    String email;
    String name;
    String nickname;
    String phoneNumber;
    UUID profileImageId;
    boolean isOnline;

    public FindUserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail().toString();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber().toString();
        this.profileImageId = user.getProfileImageId();
        this.isOnline = user.getUserStatus().checkAccess();
    }
}