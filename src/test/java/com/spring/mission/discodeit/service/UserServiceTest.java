package com.spring.mission.discodeit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.util.BinaryContentUtils;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  //단위테스트: 비즈니스 로직이 원하는 결과를 도출하는지 <-확인!
  //@Mock으로 외부 의존성 대체 ,
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStatusService userStatusService;
  @Mock
  private UserMapper userMapper;

  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentUtils binaryContentUtils;

  @InjectMocks
  private BasicUserService userService;
  //given
  // when ..  thenReturn
  //verity -> 어떤 메서드가 몇 번/어떤 파라미터로 호출되었는지 확인 가능

  @Nested
  @DisplayName("유저생성")
  class UserCreate {

    private UserRequest request;
    private User user;
    private UserDto userDto;


    @BeforeEach
    void setUp() {
      request = new UserRequest(
          "testUser", "1234", "test@test.com"
      );
      user = User.builder()
          .username("testUser")
          .password("1234")
          .email("test@test.com")
          .profile(null).build();
      userDto = UserDto.builder()
          .id(UUID.randomUUID())
          .name("testUser")
          .email("test@test.com")
          .profile(null)
          .online(true)
          .build();
    }

    @Test
    @DisplayName("유저생성 성공")
    void testCreateUser() {
      // Given , mock의 동작을 설정한다.
      given(userRepository.existsByEmail(request.email())).willReturn(false);
      given(userRepository.existsByUsername(request.name())).willReturn(false);
      given(userRepository.save(any(User.class))).willReturn(user);
      given(userMapper.toDto(user)).willReturn(userDto);
      BinaryContent mockProfile = null;
      given(binaryContentUtils.makeNullableProfile(Optional.empty()))
          .willReturn(mockProfile);

      // When
      UserDto result = userService.createUser(request, Optional.empty());

      // Then
      assertEquals(result, userDto);
      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo(request.name());
      assertThat(result.getEmail()).isEqualTo(request.email());
      then(userRepository).should().save(any(User.class));
    }

    @Nested
    @DisplayName("유저 업데이트")
    class UserUpdate {

    }
  }

}
