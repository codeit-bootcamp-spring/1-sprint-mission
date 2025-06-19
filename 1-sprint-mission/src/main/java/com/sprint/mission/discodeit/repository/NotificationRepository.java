package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  List<Notification> findByReceiverId(UUID receiverId);

  Optional<Notification> findByIdAndReceiverId(UUID id, UUID receiverId);

  List<Notification> findAllByReceiverId(UUID userId);
}
