package com.sprint.mission.discodeit.init;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@DependsOn("passwordEncoder")
public class AdminAccountInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin";

        if (userRepository.existsByUsername(adminUsername)) {
            return;
        }

        User admin = new User(
                adminUsername,
                "admin@abc.com",
                passwordEncoder.encode("admin123"),
                null,
                Role.ADMIN
                );
        userRepository.save(admin);
        log.info("admin 관리자 생성 완료");
    }
}
