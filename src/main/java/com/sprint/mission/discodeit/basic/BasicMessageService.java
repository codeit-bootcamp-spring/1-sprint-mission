package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    @Override
    @Transactional
    public MessageDto createMessage(MessageDto messageDTO) {
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setChannelId(messageDTO.getChannelId());
        message.setSenderId(messageDTO.getSenderId());
        message.setContent(messageDTO.getContent());
        message.setCreatedAt(LocalDateTime.now());

        try {
            UserDto user = userService.find(messageDTO.getSenderId());
            message.setSenderName(user.getName());
        } catch (Exception e) {
            message.setSenderName("Unknown Sender");
        }

        Message savedMessage = messageRepository.save(message);
        return convertToDTO(savedMessage);
    }

    private MessageDto convertToDTO(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setChannelId(message.getChannelId());
        dto.setChannelName(message.getChannelName());
        dto.setSenderId(message.getSenderId());
        dto.setSenderName(message.getSenderName());
        dto.setContent(message.getContent());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setUpdatedAt(message.getUpdatedAt());
        return dto;
    }

    @Override
    public List<MessageDto> getChannelMessages(String channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageDto updateMessage(String id, MessageDto messageDTO) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setContent(messageDTO.getContent());
        message.setUpdatedAt(LocalDateTime.now());

        Message updatedMessage = messageRepository.save(message);
        return convertToDTO(updatedMessage);
    }

    @Override
    public List<MessageDto> findAllByChannelId(String channelId) {
        return messageRepository.findAllByChannelId(channelId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMessage(String id) {
        messageRepository.deleteById(id);
    }



    @Override
    public List<MessageDto> findAll() {
        return messageRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}