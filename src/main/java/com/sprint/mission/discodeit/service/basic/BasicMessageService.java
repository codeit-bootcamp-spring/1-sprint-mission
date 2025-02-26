package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
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
    public Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> binaryContentRequests) {
        if (!userRepository.existsById(messageCreateRequest.authorId())) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
        }
        if (!channelRepository.existsById(messageCreateRequest.channelId())) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다.");
        }
        validator.validate(messageCreateRequest.content());

        List<UUID> binaryContentIds = binaryContentRequests.stream()
                .map(binaryContentRequest -> binaryContentService.create(binaryContentRequest).getId())
                .toList();

        return messageRepository.save(new Message(messageCreateRequest.content(),
                messageCreateRequest.authorId(),
                messageCreateRequest.channelId(),
                binaryContentIds));
    }

    @Override
    public Message find(UUID messageId) {
        Message message = Optional.ofNullable(messageRepository.find(messageId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다."));
        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다.");
        }

        return messageRepository.findAllByChannelId(channelId).stream()
                .toList();
    }

    @Override
    public List<Message> findAllByAuthorId(UUID authorId) {
        if (!userRepository.existsById(authorId)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
        }

        return messageRepository.findAllByAuthorId(authorId).stream()
                .toList();
    }


    @Override
    public Message update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        Message message = messageRepository.find(messageId);
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
