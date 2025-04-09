package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public MessageDto createMessage(MessageRequest messageRequest,
      List<BinaryContentRequest> attachmentRequests) {
    Channel foundChannel = channelRepository.findById(messageRequest.channelId()).orElseThrow(()
        -> new NoSuchElementException(messageRequest.channelId() + "does not exist"));

    User foundUser = userRepository.findById(messageRequest.userId()).orElseThrow(()
        -> new NoSuchElementException(messageRequest.userId() + "does not exist"));

    List<BinaryContent> attachments = attachmentRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          log.info("Message's Image :{} created", binaryContent.getId());
          return binaryContent;
        })
        .toList();

    //builder를 통한 message만들기
    Message message = Message.builder()
        .content(messageRequest.content())
        .channel(foundChannel)
        .author(foundUser)
        .attachments(attachments)
        .build();

    log.debug("DEBUG: Message created : {}", message);
    log.info("Message created with id : {}", message.getId());
    return messageMapper.toDto(messageRepository.save(message));
  }

  @Override
  public MessageDto findById(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Message not found"));
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
            Optional.ofNullable(createAt).orElse(Instant.now()),
            pageable)
        .map(messageMapper::toDto);

    Instant nextCursor = null;
    if (!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1)
          .createdAt();
    }

    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Override
  public MessageDto update(UUID id, MessageUpdateRequest messageUpdateRequest) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Message not found"));

    message.updateContent(messageUpdateRequest.content());
    log.debug("DEBUG: Message updated : {}", message);
    log.info("Message updated with id : {}", message.getId());
    return messageMapper.toDto(messageRepository.save(message));
  }

  @Override
  public void deleteMessage(UUID msgID) {
    messageRepository.deleteById(msgID);
    log.info("Message deleted with id : {}", msgID);
  }
}
