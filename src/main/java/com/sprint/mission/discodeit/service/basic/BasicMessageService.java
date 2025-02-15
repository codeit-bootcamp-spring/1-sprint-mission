package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validator.MessageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageValidator validator;

    private final BinaryContentService binaryContentService;

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public MessageResponseDto create(MessageCreateRequestDto messageCreateRequestDto) {
        userRepository.existsById(messageCreateRequestDto.authorId());
        channelRepository.existsById(messageCreateRequestDto.channelId());
        validator.validate(messageCreateRequestDto.content());

        List<UUID> binaryContentData = messageCreateRequestDto.binaryContentData().stream()
                .map(binaryContentRequestDto -> binaryContentService.create(binaryContentRequestDto).getId())
                .toList();
        Message message = messageRepository.save(new Message(messageCreateRequestDto.content(),
                messageCreateRequestDto.authorId(),
                messageCreateRequestDto.channelId(),
                binaryContentData));

        return getMessageInfo(message);
    }

    @Override
    public Message find(UUID messageId) {
        return Optional.ofNullable(messageRepository.find(messageId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다."));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public MessageResponseDto getMessageInfo(Message message) {
        List<byte[]> binaryContentData = message.getBinaryContentData().stream()
                .map(binaryContentId -> binaryContentService.find(binaryContentId).getData())
                .toList();
        return MessageResponseDto.from(message.getId(), message.getContent(), message.getAuthorId(), message.getChannelId(), binaryContentData);
    }

    @Override
    public void update(UUID messageId, String content) {
        Message message = find(messageId);
        messageRepository.update(message, content);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = find(messageId);
        messageRepository.delete(message);
    }
}
