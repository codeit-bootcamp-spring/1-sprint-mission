package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserCommonRequest;
import com.sprint.mission.discodeit.entity.user.UserLoginRequest;
import com.sprint.mission.discodeit.exception.user.IllegalUserException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.status.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStateService userStateService;
    @Mock
    private BinaryContentService binaryContentService;

    private AuthService authService;
    private UserService userService;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        authService = new UserAuthService(userRepository);
        userService = new BasicUserService(userRepository, userStateService, binaryContentService);

        user1 = new User("userA", "test@gmail.com", "password");
        user2 = new User("userB", "test2@gmail.com", "password");

        when(userRepository.findAllUser()).thenReturn(new HashMap<>());
        when(userStateService.create(any())).thenReturn(new UserStatus(user1.getId()));
        when(userRepository.userSave(any(User.class))).thenReturn(user1);
    }

    @Test
    @DisplayName("유저 로그인 성공")
    void login() {
        UserCommonRequest userCommonRequest = new UserCommonRequest(user1.getUserName(), user1.getUserEmail(), user1.getUserPassword());
        User createdUser = userService.createUser(userCommonRequest);

        Map<UUID, User> userMap = new HashMap<>();
        userMap.put(createdUser.getId(), createdUser);
        when(userRepository.findAllUser()).thenReturn(userMap);

        UserLoginRequest loginRequest = new UserLoginRequest(createdUser.getUserName(), createdUser.getUserPassword());

        User loginUser = authService.login(loginRequest);

        assertThat(loginUser.getUserName()).isEqualTo(createdUser.getUserName());
        assertThat(loginUser.getUserPassword()).isEqualTo(createdUser.getUserPassword());
    }

    @Test
    @DisplayName("잘못된 유저 로그인")
    void failLogin() {
        UserCommonRequest userCommonRequest = new UserCommonRequest(user1.getUserName(), user1.getUserEmail(), user1.getUserPassword());
        User createdUser = userService.createUser(userCommonRequest);

        Map<UUID, User> userMap = new HashMap<>();
        userMap.put(createdUser.getId(), createdUser);
        when(userRepository.findAllUser()).thenReturn(userMap);

        UserLoginRequest failLoginRequest = new UserLoginRequest("USER_A", "유저A123!@");

        assertThatThrownBy(() -> authService.login(failLoginRequest))
                .isInstanceOf(IllegalUserException.class);
    }
}