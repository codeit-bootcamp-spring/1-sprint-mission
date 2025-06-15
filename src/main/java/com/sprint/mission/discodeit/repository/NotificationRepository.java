package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> getAllByReceiverId(UUID receiverId);

    void deleteByIdAndReceiverId(UUID id, UUID receiverId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.receiverId = :receiverId")
    void deleteAllByReceiverId(@Param("receiverId") UUID receiverId);
}
