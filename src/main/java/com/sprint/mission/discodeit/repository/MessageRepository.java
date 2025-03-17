package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  @Query("SELECT m FROM Message m WHERE (:createdAt is null or m.createdAt < :createdAt) ORDER BY m.createdAt DESC, m.id DESC")
  Page<Message> findAllByCursor(Instant createdAt, Pageable pageable);
}
