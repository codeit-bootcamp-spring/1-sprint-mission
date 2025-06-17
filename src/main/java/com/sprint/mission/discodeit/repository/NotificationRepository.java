package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  boolean existsByReceiverIdAndTargetIdAndType(User user, UUID id, NotificationType notificationType);
}
