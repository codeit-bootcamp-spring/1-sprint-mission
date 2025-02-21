package com.example.mission.discodeit.repository;

import com.example.mission.discodeit.entity.MessageReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageReceiptRepository extends JpaRepository<MessageReceipt, Long> {
    List<MessageReceipt> findByUserId(UUID userId);
}