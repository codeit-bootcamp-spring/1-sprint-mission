package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Notification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("select n from Notification n "
        + "where n.receiver.id = :receiverId "
        + "order by n.createdAt desc ")
    List<Notification> findAllByReceiverIdOrderByCreatedAtDesc(
        @Param("receiverId") UUID receiverId);


    @Query("select n from Notification n "
        + "left join fetch n.receiver "
        + "where n.id = :notificationId")
    Optional<Notification> findByIdWithReceiver(@Param("notificationId") UUID notificationId);
}
