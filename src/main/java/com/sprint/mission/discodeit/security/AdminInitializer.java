package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@example.com");
            user.setPassword(passwordEncoder.encode("admin1234!@"));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
        if (userRepository.findByEmail("user@example.com").isEmpty()) {
            User user = new User();
            user.setUsername("우디");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("123qwe!@"));
            user.setRole(Role.CHANNEL_MANAGER);
            userRepository.save(user);
        }
    }
}
