package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;
    private final String adminEmail;

    public AdminInitializer(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        @Value("${discodeit.admin.username}") String adminUsername,
        @Value("${discodeit.admin.password}") String adminPassword,
        @Value("${discodeit.admin.email}") String adminEmail) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.adminEmail = adminEmail;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeAdmin();
    }

    private void initializeAdmin() {

        if (userRepository.existsByRole(Role.ADMIN)) {
            log.info("관리자 계정이 이미 존재합니다.");
            return;
        }

        if (userRepository.existsByEmail(adminEmail)) {
            log.warn("이미 사용 중인 이메일입니다. 관리자 계정을 생성할 수 없습니다.");
            return;
        }

        User admin = User.createUserWithRole(
            adminUsername,
            passwordEncoder.encode(adminPassword),
            adminEmail,
            null,
            Role.ADMIN
        );

        userRepository.save(admin);
        log.info("관리자 계정이 생성되었습니다. Username: {}", adminUsername);
    }
}
