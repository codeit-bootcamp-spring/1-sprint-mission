package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("select n from Notification n "
        + "join fetch n.receiver "
        + "join fetch n.target "
        + "where n.receiver.id = :receiverId")
    List<Notification> findAllByReceiverId(UUID receiverId);
}
