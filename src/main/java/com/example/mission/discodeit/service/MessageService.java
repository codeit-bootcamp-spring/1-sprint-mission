package com.example.mission.discodeit.service;

import com.example.mission.discodeit.entity.Message;
import com.example.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChannel(Long channelId) {
        return messageRepository.findByChannelId(channelId);
    }

    public Message updateMessage(Long id, String content) {
        return messageRepository.findById(id).map(message -> {
            message.setContent(content);
            return messageRepository.save(message);
        }).orElseThrow(() -> new RuntimeException("Message not found"));
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
