package com.sprint.mission.discodeit.component;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.eenum.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Init {
  private final UserService userService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  public void initAdmin() {
    String adminUsername = "admin";
    String adminEmail = "admin@admin.com";

    UserCreateRequest userCreateRequest=new UserCreateRequest(adminUsername,adminEmail,"tmdcks1234");
    boolean exists = userRepository.existsByUsername(adminUsername);
    if (exists) return;
    UserDto user=userService.create(userCreateRequest,null);
    userService.updateUserRole(user.id(),Role.ROLE_ADMIN);
  }
}
