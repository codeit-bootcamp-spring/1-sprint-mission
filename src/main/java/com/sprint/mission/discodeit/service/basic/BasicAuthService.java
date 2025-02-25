package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserResponse login(UserRequest.Login request) {
        User findUser = userRepository.findByName(request.name())
                .orElseThrow(() -> new RestApiException(ErrorCode.LOGIN_FAILED, "User does not exist, or entered the wrong ID"));

        if (!findUser.getPassword().equals(request.password())) {
            throw new RestApiException(ErrorCode.LOGIN_FAILED, "Entered the wrong password.");
        }

        log.info("user login : {}", findUser.getId());
        return userService.findById(findUser.getId());
    }
}
