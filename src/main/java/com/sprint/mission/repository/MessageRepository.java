package com.sprint.mission.repository;

import com.sprint.mission.entity.main.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
//Message save(Message message);
//
//Optional<Message> findById(UUID id);
//List<Message> findAll();
//void delete(UUID messageId);
//
//boolean existsById(UUID messageId);