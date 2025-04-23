package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.EmailAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BasicUserServiceTest {

  private static final Logger logger = LoggerFactory.getLogger(BasicUserServiceTest.class);

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicUserService userService;

  private UUID userId;
  private String username;
  private String email;
  private String password;
  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    username = "testUser";
    email = "test@example.com";
    password = "password123";

    // ReflectionTestUtils 대신 간단한 테스트 헬퍼 메서드 사용
    user = createTestUser(userId, username, email, password);
    userDto = new UserDto(userId, username, email, null, true);
  }

  // 테스트용 유틸리티 메서드 - ReflectionTestUtils 대신 사용
  private User createTestUser(UUID id, String username, String email, String password) {
    User user = new User(username, email, password, null);
    // ID는 테스트용으로 직접 설정
    // 실제로는 엔티티에 테스트용 setter를 추가하거나 테스트용 생성자를 만드는 것이 더 좋음
    try {
      java.lang.reflect.Field idField = User.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(user, id);
    } catch (Exception e) {
      logger.error("Failed to set ID field", e);
    }
    return user;
  }

  @Test
  @Order(1)
  @DisplayName("사용자 생성 성공")
  void createUser_Success() {
    logger.info("==== 사용자 생성 성공 테스트 시작 ====");

    // given - 간략한 설명 추가
    // 이메일과 사용자명이 존재하지 않는다고 가정
    UserCreateRequest request = new UserCreateRequest(username, email, password);
    given(userRepository.existsByEmail(eq(email))).willReturn(false);
    given(userRepository.existsByUsername(eq(username))).willReturn(false);

    // 저장된 User를 Dto로 변환하는 매퍼 mock
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    // when - 사용자 생성 서비스 메서드 호출
    UserDto result = userService.create(request, Optional.empty());

    // then - 결과 검증
    assertThat(result).isEqualTo(userDto);
    verify(userRepository).save(any(User.class));

    logger.info("==== 사용자 생성 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(2)
  @DisplayName("이미 존재하는 이메일로 사용자 생성 시도 시 실패")
  void createUser_WithExistingEmail_ThrowsException() {
    logger.info("==== 이미 존재하는 이메일로 사용자 생성 시도 시 실패 테스트 시작 ====");

    // given - 이미 존재하는 이메일이라고 가정
    UserCreateRequest request = new UserCreateRequest(username, email, password);
    given(userRepository.existsByEmail(eq(email))).willReturn(true);

    // when & then - 예외가 발생하는지 검증
    assertThatThrownBy(() -> userService.create(request, Optional.empty()))
        .isInstanceOf(EmailAlreadyExistsException.class);

    logger.info("==== 이미 존재하는 이메일로 사용자 생성 시도 시 실패 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(3)
  @DisplayName("이미 존재하는 사용자명으로 사용자 생성 시도 시 실패")
  void createUser_WithExistingUsername_ThrowsException() {
    logger.info("==== 이미 존재하는 사용자명으로 사용자 생성 시도 시 실패 테스트 시작 ====");

    // given - 이메일은 존재하지 않지만 사용자명은 존재하는 경우
    UserCreateRequest request = new UserCreateRequest(username, email, password);
    given(userRepository.existsByEmail(eq(email))).willReturn(false);
    given(userRepository.existsByUsername(eq(username))).willReturn(true);

    // when & then - 예외가 발생하는지 검증
    assertThatThrownBy(() -> userService.create(request, Optional.empty()))
        .isInstanceOf(UserAlreadyExistsException.class);

    logger.info("==== 이미 존재하는 사용자명으로 사용자 생성 시도 시 실패 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(4)
  @DisplayName("사용자 조회 성공")
  void findUser_Success() {
    logger.info("==== 사용자 조회 성공 테스트 시작 ====");

    // given - 사용자가 존재한다고 가정
    given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    // when - 사용자 조회 실행
    UserDto result = userService.find(userId);

    // then - 결과 검증
    assertThat(result).isEqualTo(userDto);

    logger.info("==== 사용자 조회 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(5)
  @DisplayName("존재하지 않는 사용자 조회 시 실패")
  void findUser_WithNonExistentId_ThrowsException() {
    logger.info("==== 존재하지 않는 사용자 조회 시 실패 테스트 시작 ====");

    // given - 사용자가 존재하지 않는다고 가정
    given(userRepository.findById(eq(userId))).willReturn(Optional.empty());

    // when & then - 예외가 발생하는지 검증
    assertThatThrownBy(() -> userService.find(userId))
        .isInstanceOf(UserNotFoundException.class);

    logger.info("==== 존재하지 않는 사용자 조회 시 실패 테스트 종료 ====");
    System.out.println("\n");
  }

  // 나머지 테스트 메서드들도 동일한 방식으로 단순화할 수 있습니다.
}