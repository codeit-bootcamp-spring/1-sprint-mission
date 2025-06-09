package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sprint.mission.discodeit.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

	@Query("select n from Notification n "
		+ "where n.receiverId = :receiverId")
	List<Notification> findAllByReceiverId(UUID receiverId);
}
