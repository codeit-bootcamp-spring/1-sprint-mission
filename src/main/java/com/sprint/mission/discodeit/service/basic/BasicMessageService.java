package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final MessageValidator validator;
  private final MessageMapper messageMapper;

  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentRequest> binaryContentRequests) {
    User author = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));

    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

    validator.validate(messageCreateRequest.content());

    List<BinaryContent> attachments = binaryContentRequests.stream()
        .map(binaryContentService::create)
        .map(dto -> binaryContentRepository.findById(dto.id()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    return messageMapper.toDto(
        messageRepository.save(
            new Message(messageCreateRequest.content(), channel, author, attachments))
    );
  }

  @Override
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다."));
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다.");
    }

    return messageRepository.findByChannelId(channelId).stream()
        .map(messageMapper::toDto)
        .toList();
  }

  @Override
  public List<MessageDto> findAllByAuthorId(UUID authorId) {
    if (!userRepository.existsById(authorId)) {
      throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
    }

    return messageRepository.findByAuthorId(authorId).stream()
        .map(messageMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다."));
    message.updateContent(request.newContent());

    return messageMapper.toDto(message);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    if (!messageRepository.existsById(messageId)) {
      throw new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다.");
    }

    messageRepository.deleteById(messageId);
  }
}
