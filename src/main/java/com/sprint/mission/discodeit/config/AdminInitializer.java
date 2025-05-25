package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;
    private final String adminEmail;

    public AdminInitializer(
        UserRepository userRepository,
        UserStatusRepository userStatusRepository,
        PasswordEncoder passwordEncoder,
        @Value("${discodeit.admin.username}") String adminUsername,
        @Value("${discodeit.admin.password}") String adminPassword,
        @Value("${discodeit.admin.email}") String adminEmail) {

        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.adminEmail = adminEmail;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeAdmin();
    }

    @Transactional
    protected void initializeAdmin() {

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
            adminEmail,
            passwordEncoder.encode(adminPassword),
            null,
            Role.ADMIN
        );
        userRepository.save(admin);

        // 삭제 예정
        UserStatus userStatus = UserStatus.createUserStatus(admin);
        userStatusRepository.save(userStatus);

        log.info("관리자 계정이 생성되었습니다. Username: {}", adminUsername);
    }
}
