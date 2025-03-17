package com.sprint.mission.discodeit.service.basic;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class BasicMessageService {

    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable) {
        return messageRepository.findByChannel_IdOrderByCreatedAtDesc(channelId, pageable);
    }
} 