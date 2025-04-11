package com.spring.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
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

  @InjectMocks
  private BasicUserService userService;
  //given
  // when ..  thenReturn
  //verity -> 어떤 메서드가 몇 번/어떤 파라미터로 호출되었는지 확인 가능

  @Nested
  @DisplayName("유저생성")
  class UserCreate {

    @Test
    @DisplayName("유저생성 성공")
    void testCreateUser() {
      //given
      UserRequest userRequest = new UserRequest(
          "name", "1234", "name@gmail.com"
      );

      UUID userId = UUID.randomUUID();
      //가짜 유저
      User savedUser = mock(User.class);

      // 2. save 동작에 대한 mock
      when(userRepository.save(any(User.class))).thenReturn(savedUser);

      // 3. toDto에 대한 mock
      UserDto userDto = UserDto.builder()
          .id(userId)
          .name("name")
          .email("name@gmail.com")
          .profile(null)
          .online(true)
          .build();
      when(userMapper.toDto(savedUser)).thenReturn(userDto);

      // when
      UserDto result = userService.createUser(userRequest, java.util.Optional.empty());

      //then
      assertNotNull(result);
      assertEquals("name", result.getName());
      assertEquals("name@gmail.com", result.getEmail());
    }
  }

  @Nested
  @DisplayName("유저 업데이트")
  class UserUpdate {

  }

}
