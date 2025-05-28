package com.sprint.mission.discodeit.initializer;

import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.common.Phone.RegionCode;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${discodeit.admin.username}")
  private String adminUsername;

  @Value("${discodeit.admin.password}")
  private String adminPassword;

  @Value("${discodeit.admin.email}")
  private String adminEmail;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    boolean exists = userRepository.existsUserByUsername(adminUsername);

    if (!exists) {
      User admin = new User(adminUsername, adminUsername, adminEmail,
          passwordEncoder.encode(adminPassword), new Phone("010-1111-2222", RegionCode.KR),
          Role.ADMIN, null, null);
      userRepository.save(admin);
    }

  }
}
