package com.sprint.mission.discodeit.service.jcf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserName;
import com.sprint.mission.discodeit.entity.user.dto.FindUserRequest;
import com.sprint.mission.discodeit.service.user.UserConverter;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserInterface smoke testing")
class JCFUserServiceTest {

    UserRepository userRepository;
    UserConverter userConverter;
    JCFUserService userService;


    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userConverter = new UserConverter();
        userService = new JCFUserService(userRepository, userConverter);
    }

    @Test
    @DisplayName("유저 이름으로 findUserByUsername 호출 시 UserResponse 반환")
    void givenUsernameWhenFindUserByUsernameThenReturnUserInfoResponse() {
        Optional<User> mockUser = Optional.of(new User(new UserName("jaewoo")));

        when(userRepository.findByUsername("jaewoo"))
                .thenReturn(mockUser);

        var user = userService.findUserByUsername(new FindUserRequest("jaewoo"));

        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo("jaewoo");

        verify(userRepository).findByUsername("jaewoo");
    }

    // 수정

    // 저장

    // 탈퇴

}