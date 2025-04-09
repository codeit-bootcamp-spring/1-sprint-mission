package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStatusRepository userStatusRepository;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private UserMapper userMapper;
  @InjectMocks
  private BasicUserService basicUserService;

  @DisplayName("유저 생성 성공 테스트")
  @Test
  void given_new_user_when_create_then_return_userDto() {
    // given
    String username = "user1";
    String email = "user1@email.com";
    String password = "pass123";
    UserCreateRequest request = new UserCreateRequest(username, email, password);

    when(userRepository.existsByEmail(email)).thenReturn(false);
    when(userRepository.existsByUsername(username)).thenReturn(false);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    User user = new User(username, email, password, null);
    UserDto expectedDto = new UserDto(UUID.randomUUID(), username, email, null, null);

    when(userMapper.toDto(any(User.class))).thenReturn(expectedDto);

    // when
    UserDto result = basicUserService.create(request, Optional.empty());

    // then
    verify(userRepository).save(userCaptor.capture());
    assertThat(userCaptor.getValue().getUsername()).isEqualTo(username);
    assertThat(result.username()).isEqualTo(username);
    verify(userStatusRepository, never()).save(any());
  }


}
