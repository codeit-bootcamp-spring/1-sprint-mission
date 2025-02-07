package com.sprint.mission.discodeit.application.service;

import com.sprint.mission.discodeit.application.dto.JoinUserReqeustDto;
import com.sprint.mission.discodeit.application.dto.UserResponseDto;
import com.sprint.mission.discodeit.application.service.converter.UserConverter;
import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.Username;
import com.sprint.mission.discodeit.domain.user.exception.AlreadyUserExistsException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.repository.user.interfaces.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserService(
        UserRepository userRepository,
        UserConverter userConverter
    ) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    public UserResponseDto join(JoinUserReqeustDto requestDto) {
        boolean isEmailAlreadyUsed = userRepository.isExistByEmail(new Email(requestDto.email()));
        if (isEmailAlreadyUsed) {
            throw new AlreadyUserExistsException(ErrorCode.DUPLICATE_EMAIL, requestDto.email());
        }

        boolean isUsernameAlreadyUsed = userRepository.isExistByUsername(new Username(requestDto.username()));
        if (isUsernameAlreadyUsed) {
            throw new AlreadyUserExistsException(ErrorCode.DUPLICATE_USERNAME, requestDto.username());
        }

        User savedUser = userRepository.save(userConverter.toUser(requestDto));
        return userConverter.toDto(savedUser);
    }
}
