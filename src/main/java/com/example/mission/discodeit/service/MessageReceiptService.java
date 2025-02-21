package com.example.mission.discodeit.service;

import com.example.mission.discodeit.entity.MessageReceipt;
import com.example.mission.discodeit.repository.MessageReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageReceiptService {
    private final MessageReceiptRepository messageReceiptRepository;

    public MessageReceipt createReceipt(MessageReceipt receipt) {
        return messageReceiptRepository.save(receipt);
    }

    public List<MessageReceipt> getReceiptsByUser(UUID userId) {
        return messageReceiptRepository.findByUserId(userId);
    }
}
