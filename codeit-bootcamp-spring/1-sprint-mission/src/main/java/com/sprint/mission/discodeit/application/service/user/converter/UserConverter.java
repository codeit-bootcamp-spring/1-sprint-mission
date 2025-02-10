package com.sprint.mission.discodeit.application.service.user.converter;

import com.sprint.mission.discodeit.application.dto.user.joinUserRequestDto;
import com.sprint.mission.discodeit.application.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.domain.user.BirthDate;
import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.Nickname;
import com.sprint.mission.discodeit.domain.user.Password;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.Username;
import com.sprint.mission.discodeit.domain.user.enums.EmailSubscriptionStatus;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User toUser(joinUserRequestDto requestDto) {
        return User.builder()
            .username(new Username(requestDto.username()))
            .nickname(new Nickname(requestDto.nickname()))
            .email(new Email(requestDto.email()))
            .password(new Password(requestDto.password()))
            .birthDate(new BirthDate(requestDto.birthDate()))
            .emailSubscriptionStatus(EmailSubscriptionStatus.UNSUBSCRIBED)
            .build();
    }

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getNicknameValue(),user.getUsernameValue(),user.getEmailValue());
    }
}
