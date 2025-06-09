package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.notification.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  List<Notification> findAllByReceiver_IdOrderByCreatedAtDesc(UUID receiverId);

  boolean existsByReceiver_id(UUID receiverId);

  boolean existsByReceiver_Id(UUID receiverId);
}
