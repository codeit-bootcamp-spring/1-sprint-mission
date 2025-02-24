package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.LoginRequest;
import com.sprint.mission.discodeit.dto.user.response.LoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import com.sprint.mission.discodeit.validator.UserStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;

    private UserValidator userValidator;
    private UserMapper userMapper;
    private UserStatusService userStatusService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        userValidator = new UserValidator(userRepository);
        userMapper = new UserMapper();
        userStatusRepository = new JCFUserStatusRepository();
        userValidator = new UserValidator(userRepository);
        userStatusService = new BasicUserStatusService(userStatusRepository,
                new UserStatusValidator(userStatusRepository), userValidator);
        authService = new BasicAuthService(userValidator, userStatusService, userMapper);
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() {
        // given
        User user = User.of("test1", "nickname1", "email1", "password1");
        userRepository.saveUser(user);
        UserStatus userStatus = userStatusRepository.saveUserStatus(UserStatus.of(user));

        LoginRequest loginRequest = new LoginRequest("test1", "password1");

        // when
        LoginResponse loginResponse = authService.login(loginRequest);

        // then
        assertEquals(loginResponse.userId(), user.getId());

        UserStatus foundUserStatus = userStatusRepository.findUserStatusById(userStatus.getId());
        assertTrue(foundUserStatus.isOnline());
    }

}