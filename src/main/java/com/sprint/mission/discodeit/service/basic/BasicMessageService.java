package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
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
    public MessageResponseDto find(UUID messageId) {
        Message message = Optional.ofNullable(messageRepository.find(messageId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다."));
        return getMessageInfo(message);
    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findAll();

        if (channelId == null) {
            return messages.stream()
                    .map(this::getMessageInfo)
                    .toList();
        }

        return messages.stream()
                .filter(message -> message.isSameChannelId(channelId))
                .map(this::getMessageInfo)
                .toList();
    }

    @Override
    public MessageResponseDto getMessageInfo(Message message) {
        List<byte[]> binaryContentData = message.getBinaryContentData().stream()
                .map(binaryContentId -> binaryContentService.find(binaryContentId).getData())
                .toList();
        return MessageResponseDto.from(message.getId(), message.getContent(), message.getAuthorId(), message.getChannelId(), binaryContentData);
    }

    @Override
    public MessageResponseDto update(MessageUpdateRequestDto messageUpdateRequestDto) {
        Message message = messageRepository.find(messageUpdateRequestDto.id());

        message.updateContent(messageUpdateRequestDto.content());
        messageRepository.save(message);

        return getMessageInfo(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = Optional.ofNullable(messageRepository.find(messageId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다."));

        message.getBinaryContentData().forEach(binaryContentService::delete);
        messageRepository.delete(messageId);
    }
}
