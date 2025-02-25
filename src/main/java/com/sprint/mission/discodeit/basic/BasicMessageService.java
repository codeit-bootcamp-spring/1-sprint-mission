package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForLocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
    public MessageDTO createMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setChannelId(messageDTO.getChannelId());
        message.setSenderId(messageDTO.getSenderId());
        message.setContent(messageDTO.getContent());
        message.setCreatedAt(LocalDateTime.now());

        try {
            Channel channel = channelService.find(messageDTO.getChannelId());
            message.setChannelName(channel.getName());
        } catch (Exception e) {
            message.setChannelName("Unknown Channel");
        }

        try {
            UserDTO user = userService.find(messageDTO.getSenderId());
            message.setSenderName(user.getName());
        } catch (Exception e) {
            message.setSenderName("Unknown Sender");
        }

        Message savedMessage = messageRepository.save(message);
        return convertToDTO(savedMessage);
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
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
    public List<MessageDTO> getChannelMessages(String channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageDTO updateMessage(String id, MessageDTO messageDTO) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setContent(messageDTO.getContent());
        message.setUpdatedAt(LocalDateTime.now());

        Message updatedMessage = messageRepository.save(message);
        return convertToDTO(updatedMessage);
    }

    @Override
    public List<MessageDTO> findAllByChannelId(String channelId) {
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
    public List<MessageDTO> findAll() {
        return messageRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}