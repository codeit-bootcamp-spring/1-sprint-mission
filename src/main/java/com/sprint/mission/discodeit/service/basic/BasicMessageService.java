package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.MessageValidator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        User author = Optional.ofNullable(userRepository.find(messageCreateRequest.authorId()))
            .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));

        Channel channel = Optional.ofNullable(channelRepository.find(messageCreateRequest.channelId()))
            .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

        validator.validate(messageCreateRequest.content());

        List<BinaryContent> attachments = binaryContentRequests.stream()
                .map(binaryContentService::create)
                .toList();

        return messageRepository.save(new Message(messageCreateRequest.content(), channel, author, attachments));
    }

    @Override
    public Message find(UUID messageId) {
      return Optional.ofNullable(messageRepository.find(messageId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다."));
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
        message.updateContent(messageUpdateRequest.newContent());

        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        find(messageId);

        messageRepository.delete(messageId);
    }
}
