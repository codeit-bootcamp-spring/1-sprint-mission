package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    
    @Query("SELECT DISTINCT m FROM Message m " +
           "LEFT JOIN FETCH m.attachments " +
           "LEFT JOIN FETCH m.author a " +
           "LEFT JOIN FETCH a.status " +
           "WHERE m.channel.id = :channelId " +
           "ORDER BY m.createdAt DESC")
    Slice<Message> findByChannel_IdOrderByCreatedAtDesc(@Param("channelId") UUID channelId, Pageable pageable);
} 