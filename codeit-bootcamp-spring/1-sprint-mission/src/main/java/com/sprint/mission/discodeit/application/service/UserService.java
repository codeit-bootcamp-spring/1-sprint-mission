package com.sprint.mission.discodeit.application.service;

import com.sprint.mission.discodeit.application.dto.JoinUserReqeustDto;
import com.sprint.mission.discodeit.application.dto.UserResponseDto;
import com.sprint.mission.discodeit.application.service.converter.UserConverter;
import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.User;
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
        boolean isExist = userRepository.isExistByEmail(new Email(requestDto.email()));
        if (isExist) {
            throw new IllegalArgumentException();
        }
        User savedUser = userRepository.save(userConverter.toUser(requestDto));
        return userConverter.toDto(savedUser);
    }
}
