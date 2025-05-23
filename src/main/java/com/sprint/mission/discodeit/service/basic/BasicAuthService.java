package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.Role;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional
  @Override
  public UserDto updateUserRole(UserRoleUpdateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> {
          return UserNotFoundException.withId(request.userId());
        });

    Role newRole = request.newRole();
    user.update(newRole);

    // 현재 쓰레드에서 갖고 있는 인증 정보 꺼내오기
    var auth = SecurityContextHolder.getContext().getAuthentication();
    //로그인 된 사용자(auth)가 있고 권한 변경한 엔티티의 유저 이름과 같다면 로그아웃 시키기
    if (auth != null && auth.getName().equals(user.getUsername())) {
      SecurityContextHolder.clearContext();
    }

    return userMapper.toDto(user);
  }

}
