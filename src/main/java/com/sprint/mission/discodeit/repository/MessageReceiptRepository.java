package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.MessageReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageReceiptRepository extends JpaRepository<MessageReceipt, UUID> {
    List<MessageReceipt> findAllByReceiverId(UUID receiverId);
    List<MessageReceipt> findAllByChannelId(UUID channelId);
}
