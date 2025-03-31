package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final PageResponseMapper pageResponseMapper;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public MessageDto create(CreateMessageRequest messageRequest,
      List<CreateBinaryContentRequest> binaryContentRequests) {
    UUID channelId = messageRequest.channelId();
    UUID authorId = messageRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.error("Channel not found : channelId={}", channelId);
              return new NoSuchElementException("Channel with id " + channelId + " does not exist");
            }
        );
    User author = userRepository.findById(authorId)
        .orElseThrow(
            () -> {
              log.error("User not found : authorId={}", authorId);
              return new NoSuchElementException("Author with id " + authorId + " does not exist");
            }
        );

    List<BinaryContent> attachments = convertToBinaryContents(binaryContentRequests);

    String content = messageRequest.content();
    Message message = new Message(
        content,
        channel,
        author,
        attachments
    );

    messageRepository.save(message);
    return messageMapper.toDto(message);
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found")
        );
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
            Optional.ofNullable(createAt).orElse(Instant.now()), pageable)
        .map(messageMapper::toDto);

    Instant nextCursor = null;
    if (!slice.getContent().isEmpty()) {  // 다음 페이지가 있는 경우 가장 마지막 요소의 생성일을 커서로 넘김
      nextCursor = slice.getContent().get(slice.getContent().size() - 1)
          .createAt();
    }

    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, UpdateMessageRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> {
              log.error("Message not found : messageId={}", messageId);
              return new NoSuchElementException("Message with id " + messageId + " not found");
            }
        );
    message.update(newContent);
    return messageMapper.toDto(message);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    if (!messageRepository.existsById(messageId)) {
      log.error("Message not found : messageId={}", messageId);
      throw new NoSuchElementException("Message with id " + messageId + " not found");
    }

    messageRepository.deleteById(messageId);
  }


  // List<CreateBinaryContentRequest> -> List<BinaryContent>
  // binaryContentRepository와 binaryContentStorage에 대한 의존성을 가지고 있어 util이 아닌 private 메서드로 분리
  private List<BinaryContent> convertToBinaryContents(
      List<CreateBinaryContentRequest> binaryContentRequests) {
    return binaryContentRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(
              fileName,
              (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .toList();
  }
}
