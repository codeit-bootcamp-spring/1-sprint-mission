package com.sprint.mission.discodeit.performance;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.Notification.NotificationType;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

@SpringBootTest
@ActiveProfiles("test")
public class CachePerformanceTest {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private CacheManager cacheManager;

    static UUID receiverId;

    @BeforeEach
    void setUpDummyNotifications() {
        receiverId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();

        // 알림 100개 저장
        for (int i = 0; i < 100; i++) {
            notificationRepository.save(
                Notification.create(receiverId, "title", "content", NotificationType.NEW_MESSAGE,
                    targetId
                ));
        }

        Cache cache = cacheManager.getCache("notifications");
        if (cache != null) {
            cache.evict(receiverId);
        }
    }

    @Test
    void 캐시_전용_전후_차이_비교() {
        StopWatch sw = new StopWatch();

        sw.start("캐시 전");
        notificationService.getMyNotifications(receiverId);
        sw.stop();

        sw.start("캐시 후");
        notificationService.getMyNotifications(receiverId);
        sw.stop();

        System.out.println(sw.prettyPrint());
    }
}
