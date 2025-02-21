package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.MessageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageValidator validator;

    private final BinaryContentService binaryContentService;

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public Message create(MessageCreateRequest messageCreateRequest) {
        if (!userRepository.existsById(messageCreateRequest.authorId())) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
        }
        if (!channelRepository.existsById(messageCreateRequest.channelId())) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다.");
        }
        validator.validate(messageCreateRequest.content());

        List<UUID> binaryContentData = messageCreateRequest.binaryContentData().stream()
                .map(binaryContentRequest -> binaryContentService.create(binaryContentRequest).getId())
                .toList();

        return messageRepository.save(new Message(messageCreateRequest.content(),
                messageCreateRequest.authorId(),
                messageCreateRequest.channelId(),
                binaryContentData));
    }

    @Override
    public MessageResponse find(UUID messageId) {
        Message message = Optional.ofNullable(messageRepository.find(messageId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다."));
        return getMessageInfo(message);
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
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
    public MessageResponse getMessageInfo(Message message) {
        List<byte[]> binaryContentData = message.getBinaryContentData().stream()
                .map(binaryContentId -> binaryContentService.find(binaryContentId).getData())
                .toList();
        return MessageResponse.from(message.getId(), message.getContent(), message.getAuthorId(), message.getChannelId(), binaryContentData);
    }

    @Override
    public Message update(MessageUpdateRequest messageUpdateRequest) {
        Message message = messageRepository.find(messageUpdateRequest.id());
        message.updateContent(messageUpdateRequest.content());

        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = Optional.ofNullable(messageRepository.find(messageId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다."));

        message.getBinaryContentData().forEach(binaryContentService::delete);
        messageRepository.delete(messageId);
    }
}
