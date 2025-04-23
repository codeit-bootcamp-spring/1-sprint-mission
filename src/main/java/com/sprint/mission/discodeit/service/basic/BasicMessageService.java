package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserRepository userRepository;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments) {
    log.debug("메시지 생성 시작: request={}", messageCreateRequest);

    UUID channelId = messageCreateRequest.channelId();
    UUID writerId = messageCreateRequest.writerId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));

    User writer = userRepository.findById(writerId)
        .orElseThrow(() -> UserNotFoundException.withId(writerId));

    List<BinaryContent> binaryContents = Optional.ofNullable(attachments)
        .filter(list -> !list.isEmpty())
        .map(files -> files.stream()
            .map(file -> {
              try {
                String fileName = file.getOriginalFilename();
                String contentType = file.getContentType();
                byte[] bytes = file.getBytes();

                BinaryContent binaryContent = new BinaryContent(fileName, contentType,
                    (long) bytes.length);
                binaryContentRepository.save(binaryContent);
                binaryContentStorage.put(binaryContent.getId(), bytes);
                return binaryContent;
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
            .toList())
        .orElse(List.of());

    Message message = new Message(
        channel,
        writer,
        messageCreateRequest.content(),
        binaryContents);
    messageRepository.save(message);
    log.info("메시지 생성 완료: id={}, channelId={}", message.getId(), channelId);
    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
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

  @Transactional(readOnly = true)
  @Override
  public MessageDto find(UUID messageId) {
    log.debug("메시지 조회 시작: id={}", messageId);
    MessageDto messageDto = messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(() -> MessageNotFoundException.withId(messageId));
    log.info("메시지 조회 완료: id={}", messageId);
    return messageDto;
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
    log.debug("메시지 수정 시작: id={}, request={}", messageId, messageUpdateRequest);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> MessageNotFoundException.withId(messageId));
    message.update(messageUpdateRequest.newContent());
    messageRepository.save(message);
    log.info("메시지 수정 완료: id={}, channelId={}", messageId, message.getChannel().getId());
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId, UUID writerId) {
    log.debug("메시지 삭제 시작: id={}", messageId);
    messageRepository.findById(messageId)
        .orElseThrow(() -> MessageNotFoundException.withId(messageId));
    messageRepository.deleteById(messageId);
    log.info("메시지 삭제 완료: id={}", messageId);
  }
}
