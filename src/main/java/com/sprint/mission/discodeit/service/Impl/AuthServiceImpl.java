package com.sprint.mission.discodeit.service.Impl;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;

  @Override
  public UserDto login(LoginRequest loginRequest) {
    try {
      // 요청 데이터 검증 (null 체크)
      if (loginRequest == null) {
        log.error("[로그인 실패] 로그인 요청 객체가 null입니다");
        throw new RestApiException(DomainErrorCode.INVALID_INPUT, "로그인 요청이 올바르지 않습니다.");
      }

      // 이메일 검증
      validateEmail(loginRequest.getEmail());

      // 비밀번호 검증
      validatePassword(loginRequest.getPassword());

      log.info("[로그인 진행] 이메일: '{}'", loginRequest.getEmail());

      // 사용자 조회
      User user = findUserByEmail(loginRequest.getEmail());

      // 비밀번호 검증
      validateCredentials(user, loginRequest.getPassword());

      log.info("[로그인 성공] 사용자: '{}', ID: '{}'", user.getName(), user.getId());

      return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    } catch (RestApiException e) {
      // 이미 적절한 예외 타입이므로 그대로 던짐
      throw e;
    } catch (Exception e) {
      // 예상치 못한 예외 처리
      log.error("[로그인 실패] 예상치 못한 오류: {}", e.getMessage(), e);
      throw new RestApiException(DomainErrorCode.AUTH_SERVER_ERROR, "로그인 처리 중 오류가 발생했습니다.");
    }
  }

  private void validateEmail(String email) {
    if (email == null || email.isBlank()) {
      log.error("[로그인 실패] 이메일이 누락되었습니다: '{}'", email);
      throw new RestApiException(DomainErrorCode.INVALID_INPUT, "이메일은 필수 입력값입니다.");
    }
  }

  private void validatePassword(String password) {
    if (password == null || password.isBlank()) {
      log.error("[로그인 실패] 비밀번호가 누락되었습니다");
      throw new RestApiException(DomainErrorCode.INVALID_INPUT, "비밀번호는 필수 입력값입니다.");
    }
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> {
          log.error("[로그인 실패] 존재하지 않는 이메일: '{}'", email);
          return new RestApiException(DomainErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다.");
        });
  }

  private void validateCredentials(User user, String password) {
    if (!password.equals(user.getPassword())) {
      log.error("[로그인 실패] 비밀번호 불일치: 사용자 '{}'", user.getEmail());
      throw new RestApiException(DomainErrorCode.PASSWORD_NOT_MATCH, "비밀번호가 일치하지 않습니다.");
    }
  }
}
