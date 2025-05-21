package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.user.UserLoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.security.UserUnauthorizedException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final UserRepository userRepository;
  //
  private final UserMapper userMapper;
  private final BCryptPasswordEncoder passwordEncoder;

  public UserDto getUserBySession() {
    log.info("세션 활용 사용자 정보 조회 시작");

    // SecurityContextHolder 로 Authentication 객체 들고오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // authentication 유효성 검증
    if (authentication == null || !authentication.isAuthenticated()
        || !(authentication.getPrincipal() instanceof UserDetails)) {
      throw new UserUnauthorizedException(Map.of(
          "isNull", authentication == null,
          "isAuthenticated", authentication != null && authentication.isAuthenticated(),
          "principal", authentication != null ? authentication.getPrincipal().toString() : "null"
      ));
    }

    Object principal = authentication.getPrincipal();
    String username = ((UserDetails) principal).getUsername();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(Map.of("username", username)));

    log.info("세션 활용 사용자 정보 조회 성공");
    return userMapper.toDto(user);
  }
}
