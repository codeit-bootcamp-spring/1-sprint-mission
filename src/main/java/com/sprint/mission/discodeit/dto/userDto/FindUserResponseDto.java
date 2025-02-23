package com.sprint.mission.discodeit.dto.userDto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FindUserResponseDto {
    UUID id;
    String email;
    String name;
    String nickname;
    String phoneNumber;
    UUID profileImageId;
    boolean isOnline;

    public static FindUserResponseDto fromEntity(User user) {
        return new FindUserResponseDto(user.getId()
                , user.getEmail().toString()
                , user.getName(), user.getNickname()
                , user.getPhoneNumber().toString()
                , user.getProfileImageId()
                , user.getUserStatus().checkAccess());
    }
}