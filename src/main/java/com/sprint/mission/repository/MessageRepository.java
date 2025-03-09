package com.sprint.mission.repository;

import com.sprint.mission.entity.main.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m WHERE m.channel.id = :channelId")
    List<Message> findAllByChannelId(@Param("channelId") UUID channelId);

    void deleteAllByChannelId(UUID channelId);
}
//Message save(Message message);
//
//Optional<Message> findById(UUID id);
//List<Message> findAll();
//void delete(UUID messageId);
//
//boolean existsById(UUID messageId);