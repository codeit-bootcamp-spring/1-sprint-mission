package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"dev", "prod"})
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        log.info("Checking for admin account initialization");

        if (userRepository.existsByUsername("admin")) {
            log.info("Admin account already exists");
            return;
        }

        String hashedPassword = passwordEncoder.encode("admin123!");
        User admin = new User("amdin", "admin@discodeit.com", hashedPassword, null,
            Role.ROLE_ADMIN);
        UserStatus userStatus = new UserStatus(admin, Instant.now());

        userRepository.save(admin);
        log.info("Admin account created successfully: username=admin, password=admin123!");
    }
}
