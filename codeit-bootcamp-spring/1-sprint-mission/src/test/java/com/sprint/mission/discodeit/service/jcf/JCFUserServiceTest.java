package com.sprint.mission.discodeit.service.jcf;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserName;
import com.sprint.mission.discodeit.entity.user.dto.FindUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;
import com.sprint.mission.discodeit.service.user.UserConverter;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("UserInterface smoke testing")
class JCFUserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserConverter userConverter;

    @InjectMocks
    JCFUserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        Optional<User> mockUser = Optional.of(new User(new UserName("jaewoo")));

        when(userRepository.findByUsername("jaewoo"))
                .thenReturn(mockUser);

        var user = userService.findUserByUsername(new FindUserRequest("jaewoo"));

        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo("jaewoo");

        verify(userRepository).findByUsername("jaewoo");
    }
}