package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public Message create(MessageDTO messageDTO) {
        userRepository.findById(messageDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        channelRepository.findById(messageDTO.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        Message message = new Message(
                messageDTO.getContent(),
                messageDTO.getUserId(),
                messageDTO.getChannelId()
        );

        return messageRepository.save(message);
    }

    @Override
    public Message update(String id, MessageDTO messageDTO) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));

        message.update(messageDTO.getContent());
        return messageRepository.save(message);
    }

    @Override
    public void delete(String id) {
        messageRepository.deleteById(id);
    }

    @Override
    public Message find(String id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
    }

    @Override
    public List<Message> findAllByChannelId(String channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }
}