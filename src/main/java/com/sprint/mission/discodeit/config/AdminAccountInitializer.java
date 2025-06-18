package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initAdmin();
    }

    private void initAdmin() {
        String adminUsername = "admin";
        String adminEmail = "admin@test.com";
        String adminPassword = "admin123";

        if (userRepository.existsUser(adminUsername)) {
            return;
        }

        User admin = User.of(
            adminUsername,
            adminEmail,
            passwordEncoder.encode(adminPassword)
        );
        admin.updateRole(Role.ADMIN);
        userRepository.saveUser(admin);

        User user = User.of(
            "test",
            "test@test.com",
            passwordEncoder.encode("12345678")
        );
        userRepository.saveUser(user);
    }
}

