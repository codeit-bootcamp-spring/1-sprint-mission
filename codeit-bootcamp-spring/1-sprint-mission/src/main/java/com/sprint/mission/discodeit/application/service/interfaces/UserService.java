package com.sprint.mission.discodeit.application.service.interfaces;

import com.sprint.mission.discodeit.application.dto.user.ChangePasswordRequestDto;
import com.sprint.mission.discodeit.application.dto.user.LoginRequestDto;
import com.sprint.mission.discodeit.application.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.application.dto.user.joinUserRequestDto;
import com.sprint.mission.discodeit.domain.user.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDto join(joinUserRequestDto requestDto);

    void login(LoginRequestDto requestDto);

    User findOneByUserIdOrThrow(UUID userId);

    List<UserResponseDto> findAll();

    void changePassword(UUID userId, ChangePasswordRequestDto requestDto);

    void quitUser(UUID userId);
}
