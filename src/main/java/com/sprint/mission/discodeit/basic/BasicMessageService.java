package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public MessageDto createMessage(MessageDto messageDTO) {
        Message message = Message.builder()
                .content(messageDTO.getContent())
                .build();

        String senderName;
        try {
            UserDto user = userService.find(messageDTO.getAuthorId());
            senderName = user.getName();
        } catch (Exception e) {
            senderName = "unknown Sender";
        }

        Message savedMessage = messageRepository.save(message);
        return messageMapper.toDto(savedMessage);
    }

    @Override
    public List<MessageDto> getChannelMessages(UUID channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messages.stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageDto updateMessage(UUID id, MessageDto messageDTO) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setContent(messageDTO.getContent());

        Message updatedMessage = messageRepository.save(message);
        return messageMapper.toDto(updatedMessage);
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId)
                .stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMessage(UUID id) {
        messageRepository.deleteById(id);
    }

    @Override
    public List<MessageDto> findAll() {
        return messageRepository.findAll()
                .stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }
}