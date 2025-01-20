package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.entity.common.Status.MODIFIED;
import static com.sprint.mission.discodeit.entity.common.Status.REGISTERED;
import static com.sprint.mission.discodeit.entity.common.Status.UNREGISTERED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.repository.jcf.user.UserRepository;
import com.sprint.mission.discodeit.entity.user.dto.FindUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.ModifyUserInfoRequest;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UnregisterUserRequest;
import com.sprint.mission.discodeit.entity.user.entity.User;
import com.sprint.mission.discodeit.service.user.UserConverter;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserInterface smoke testing")
class JCFUserServiceTest {
    private static final String NAME = "SB_1기_백재우";

    private UserRepository userRepository;
    private UserConverter userConverter;
    private JCFUserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userConverter = UserConverter.getInstance();
        userService = new JCFUserService(userRepository, userConverter);
        user = User.createFrom(NAME);
    }

    @Test
    @DisplayName("유저 등록 userService register 호출 시 userInfoResponse 반환")
    void givenNewUserRequestWhenRegisterThenReturnUserInfoResponse() {
        // given
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(NAME);
        var registerUser = userConverter.toEntity(registerUserRequest);
        when(userRepository.save(any(User.class))).thenReturn(registerUser);

        // when
        var infoResponse = userService.register(registerUserRequest);

        // then
        assertAll(
                () -> {
                    assertThat(infoResponse).isNotNull();

                    assertAll(
                            () -> assertThat(infoResponse.username()).isEqualTo(NAME),
                            () -> assertThat(infoResponse.status()).isEqualTo(REGISTERED)
                    );
                }
        );
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("유저 이름으로 findUserByUsername 호출 시 UserResponse 반환")
    void givenUsernameWhenFindUserByUsernameThenReturnUserInfoResponse() {
        // given
        Optional<User> mockUser = Optional.of(user);
        var findUserRequest = new FindUserRequest(NAME);
        when(userRepository.findByUsername(NAME)).thenReturn(mockUser);

        // when
        var user = userService.findUserByUsernameOrThrow(findUserRequest);

        // then
        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo(NAME);

        verify(userRepository).findByUsername(NAME);
    }
    // TODO : 해지된 유지이름으로 조회 시 실패 테스트

    @Test
    @DisplayName("유저 정보 수정 modifyUserInfo 호출 시 UserResponse 반환")
    void givenModifyUserInfoRequestWhenModifyUserInfoThenReturnUserInfoResponse() {
        // given
        var modifyUserInfoRequest = new ModifyUserInfoRequest(user.getId(), "CHANGE NAME");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        var userInfoResponse = userService.modifyUserInfo(modifyUserInfoRequest);

        // then
        assertAll(
                () -> {
                    assertThat(userInfoResponse).isNotNull();

                    assertAll(
                            () -> assertThat(userInfoResponse.status()).isEqualTo(MODIFIED),
                            () -> assertThat(userInfoResponse.username()).isEqualTo("CHANGE NAME")
                    );
                }
        );

        verify(userRepository).save(any(User.class));
        verify(userRepository).findById(user.getId());
    }

    // 탈퇴
    @Test
    @DisplayName("유저 회원 탈퇴 unregister 호출 시 유저 상태 변경")
    void givenUnregisterUserRequestWhenUnregisterThenUserStatusIsChanged() {
        // given
        var unregisterUserRequest = new UnregisterUserRequest(user.getId(), NAME);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        userService.UnRegisterUser(unregisterUserRequest);

        // then
        assertThat(user.getStatus()).isEqualTo(UNREGISTERED);
        verify(userRepository).findById(user.getId());
        verify(userRepository).save(any(User.class));
    }

}