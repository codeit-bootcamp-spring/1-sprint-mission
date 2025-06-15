package com.sprint.mission.discodeit.service.basic;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.Notification.NotificationType;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BasicAuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @Commit
    void 유저_권한_변경_시_알림_생성까지_확인() {
        // given
        User user = userRepository.saveAndFlush(
            User.createUserWithoutProfile("test", "email", "pw"));
        UUID userId = user.getId();
        UserRoleUpdateRequest request = new UserRoleUpdateRequest(userId, Role.ADMIN);

        // when
        authService.changeUserRole(request);

        //then
        await().atMost(3, SECONDS).untilAsserted(() -> {
            List<Notification> notifications = notificationRepository.findAll();
            assertThat(notifications).anyMatch(n ->
                n.getReceiverId().equals(userId) &&
                    n.getType() == NotificationType.ROLE_CHANGED
            );
        });
    }
}