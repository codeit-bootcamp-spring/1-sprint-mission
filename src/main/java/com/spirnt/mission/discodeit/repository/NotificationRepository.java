package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.entity.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("SELECT n FROM Notification n where n.receiver.id = :receiverId")
    List<Notification> findAllByReceiverId(@Param("receiverId") UUID userId);
}
