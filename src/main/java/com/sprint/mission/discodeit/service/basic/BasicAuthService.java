package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.user.UserLoginDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusService userStatusService;

    @Override
    public UserResponseDto login(UserLoginDto userLoginDto) throws CustomException {
        User user = userRepository.findById(userLoginDto.username());
        if(user == null || !user.getPassword().equals(userLoginDto.password()) ) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        // 이것도 마찬가지로 어할 수 없는 값이라 이 방식을 쓰면 안되는지?
        // User 접속 시간 갱신 및 온라인 정보 가져오기
        boolean isUserOnline = userStatusService.updateByUserId(user.getId(), new UpdateUserStatusDto(user.getId(), Instant.now())).isActive();

        return UserResponseDto.from(user, isUserOnline);
    }
}
