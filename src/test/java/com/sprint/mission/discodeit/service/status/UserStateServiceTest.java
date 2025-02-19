package com.sprint.mission.discodeit.service.status;

import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import com.sprint.mission.discodeit.entity.status.user.UserStatusRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserStateServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @InjectMocks
    private UserStateService userStateService;

    private UUID userId;
    private User user;
    private UserStatus userStatus;
    private UserStatusRequest request;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User(userId);
        userStatus = new UserStatus(userId);
        request = new UserStatusRequest(userId, "online");
    }

    @Test
    @DisplayName("UserStatus 생성")
    void createSuccess() {
        when(userRepository.findUserById(userId)).thenReturn(user);
        when(userStatusRepository.findById(userId)).thenReturn(null);
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(userStatus);

        UserStatus result = userStateService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        verify(userStatusRepository).save(any(UserStatus.class));
    }

    @Test
    @DisplayName("존재하지 않는 User로 UserStatus 생성 시 예외 발생")
    void createFailWhenUserNotFound() {
        when(userRepository.findUserById(userId)).thenReturn(null);

        assertThatThrownBy(() -> userStateService.create(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("유저가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("이미 존재하는 UserStatus 생성 시 예외 발생")
    void createFailWhenUserStatusExists() {
        when(userRepository.findUserById(userId)).thenReturn(user);
        when(userStatusRepository.findById(userId)).thenReturn(userStatus);

        assertThatThrownBy(() -> userStateService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 유저입니다.");
    }

    @Test
    @DisplayName("잘못된 request 객체로 생성 시 예외 발생")
    void createFailWithInvalidRequest() {
        Object invalidRequest = new Object();

        assertThatThrownBy(() -> userStateService.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("UserStatusRequest 객체가 아닙니다.");
    }

    @Test
    @DisplayName("ID로 UserStatus 조회 성공")
    void findSuccess() {
        when(userStatusRepository.findById(userId)).thenReturn(userStatus);

        UserStatus result = userStateService.find(userId);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("null ID로 조회 시 예외 발생")
    void findFailWithNullId() {
        assertThatThrownBy(() -> userStateService.find(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id를 입력해주세요.");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 예외 발생")
    void findFailWithNonExistentId() {
        when(userStatusRepository.findById(userId)).thenReturn(null);

        assertThatThrownBy(() -> userStateService.find(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("찾는 UserStatus가 없습니다.");
    }

    @Test
    @DisplayName("모든 UserStatus 조회 성공")
    void findAllSuccess() {
        Map<UUID, UserStatus> userStatusMap = new HashMap<>();
        userStatusMap.put(userId, userStatus);
        when(userStatusRepository.findAll()).thenReturn(userStatusMap);

        Map<UUID, UserStatus> result = userStateService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(userId)).isEqualTo(userStatus);
    }

    @Test
    @DisplayName("UserStatus 업데이트 성공")
    void updateSuccess() {
        when(userRepository.findUserById(userId)).thenReturn(user);
        when(userStatusRepository.updateState(eq(userId), any(UserStatus.class))).thenReturn(userStatus);

        UserStatus result = userStateService.update(request);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        verify(userStatusRepository).updateState(eq(userId), any(UserStatus.class));
    }

    @Test
    @DisplayName("잘못된 request 객체로 업데이트 시 예외 발생")
    void updateFailWithInvalidRequest() {
        Object invalidRequest = new Object();

        assertThatThrownBy(() -> userStateService.update(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("UserStatusRequest 객체가 아닙니다.");
    }

    @Test
    @DisplayName("UserId로 상태 업데이트 성공")
    void updateByUserIdSuccess() {
        when(userStatusRepository.findById(userId)).thenReturn(userStatus);
        when(userStatusRepository.updateState(eq(userId), any(UserStatus.class))).thenReturn(userStatus);

        UserStatus result = userStateService.updateByUserId(userId);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        verify(userStatusRepository).updateState(eq(userId), any(UserStatus.class));
    }

    @Test
    @DisplayName("null UserId로 상태 업데이트 시 예외 발생")
    void updateByUserIdFailWithNullId() {
        assertThatThrownBy(() -> userStateService.updateByUserId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id를 입력해주세요.");
    }

    @Test
    @DisplayName("UserStatus 삭제 성공")
    void deleteSuccess() {
        userStateService.delete(userId);

        verify(userStatusRepository).remove(userId);
    }

    @Test
    @DisplayName("null ID로 삭제 시 예외 발생")
    void deleteFailWithNullId() {
        assertThatThrownBy(() -> userStateService.delete(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id를 입력해주세요.");
    }
}