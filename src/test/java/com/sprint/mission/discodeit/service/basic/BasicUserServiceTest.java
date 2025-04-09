package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.EmailAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  UserRepository userRepository;
  @Mock
  UserStatusRepository userStatusRepository;
  @Mock
  UserMapper userMapper;
  @Mock
  BinaryContentRepository binaryContentRepository;
  @Mock
  BinaryContentStorage binaryContentStorage;

  @InjectMocks
  BasicUserService basicUserService;


  @Test
  void create_successfully() {
    // given: 사용자 생성 요청
    UserCreateRequest request = new UserCreateRequest("테스트", "test@email.com", "1234");
    // 이메일, 사용자명 중복이 없도록 설정
    given(userRepository.existsByEmail(anyString())).willReturn(false);
    given(userRepository.existsByUsername(anyString())).willReturn(false);
    // 저장 요청 시 그대로 반환
    given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));
    // 저장된 user를 DTO로 변환해 반환
    given(userMapper.toDto(any(User.class))).willReturn(
        new UserDto(UUID.randomUUID(), "테스트", "test@email.com", null, true));

    // when: 서비스 메서드 호출
    UserDto result = basicUserService.create(request, Optional.empty());

    // then
    assertThat(result).isNotNull();
    assertThat(result.username()).isEqualTo("테스트");
    verify(userRepository).save(any(User.class)); // 저장 호출 확인
    verify(userMapper).toDto(any(User.class));    // DTO 변환 확인
  }

  @Test
  void create_fails_whenEmailExists() {
    // given: 이미 등록된 이메일일 경우 설정
    UserCreateRequest request = new UserCreateRequest("귀요미", "test@email.com", "1234");
    given(userRepository.existsByEmail("test@email.com")).willReturn(true);

    // when & then: 예외가 발생해야 함
    assertThrows(EmailAlreadyExistsException.class, () -> {
      basicUserService.create(request, Optional.empty());
    });
    verify(userRepository).existsByEmail("test@email.com"); // 중복 검사 호출 확인
  }

  @Test
  void update_successfully() {
    // given: 유저 ID와 변경 요청 객체 생성
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("newName", "new@email.com", "newpass");

    // 기존 유저 생성
    User user = new User("oldName", "old@email.com", "oldPass", null);

    // 유저 조회 및 중복 검사 통과 설정
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userRepository.existsByEmail("new@email.com")).willReturn(false);
    given(userRepository.existsByUsername("newName")).willReturn(false);

    // DTO로 변환하여 결과 리턴
    given(userMapper.toDto(any(User.class))).willReturn(
        new UserDto(userId, "newName", "new@email.com", null, true));

    // when: 업데이트 메서드 호출
    UserDto result = basicUserService.update(userId, request, Optional.empty());

    // then: 결과 확인
    assertThat(result).isNotNull();
    assertThat(result.username()).isEqualTo("newName");
    verify(userRepository, times(1)).findById(userId); // 조회 확인
    verify(userMapper, times(1)).toDto(any(User.class)); // 변환 호출 확인
  }

  @Test
  void update_fails_whenUserNotFound() {
    // given: 존재하지 않는 유저 ID
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("newName", "new@email.com", "newpass");

    // 조회 시 결과 없음
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when & then: 예외 발생 확인
    assertThrows(UserNotFoundException.class, () -> {
      basicUserService.update(userId, request, Optional.empty());
    });
    verify(userRepository, times(1)).findById(userId); // 조회 시도 확인
  }

  @Test
  void delete_successfully() {
    // given: 사용자 ID가 존재한다고 가정
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(true); // 존재 여부 true 반환// delete 동작 설정

    // when: delete 메서드 실행
    basicUserService.delete(userId);

    // then: existsById와 deleteById가 호출되었는지 검증
    verify(userRepository).existsById(userId); // 호출 여부 직접 검증
    verify(userRepository).deleteById(userId); // 호출 여부 직접 검증
  }

  @Test
  void delete_fails_whenUserNotFound() {
    // given: 사용자 ID가 존재하지 않는다고 가정
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(false); // 존재 여부 false 반환

    // when & then: 예외가 발생해야 함
    assertThatThrownBy(() -> basicUserService.delete(userId))
        .isInstanceOf(UserNotFoundException.class); // 예외 타입 확인

    //existsById는 호출되었지만 deleteById는 호출되지 않아야 함
    verify(userRepository, times(1)).existsById(userId); // 호출 여부 검증
    verify(userRepository, never()).deleteById(any()); // delete는 호출되면 안 됨
  }
}