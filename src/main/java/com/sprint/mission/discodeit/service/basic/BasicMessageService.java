package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotfoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final MessageMapper messageMapper;

  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  private final PageResponseMapper pageResponseMapper;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> fileAttachments) {
    User author = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> new UserNotFoundException(
            ErrorCode.USER_NOT_FOUND,
            Map.of("userId", messageCreateRequest.authorId())
        ));
    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> new ChannelNotFoundException(
            ErrorCode.CHANNEL_NOT_FOUND,
            Map.of("channelId", messageCreateRequest.channelId())
        ));

    List<BinaryContent> attachments = Optional.ofNullable(fileAttachments)
        .map(files -> files.stream()
            .map(binaryContentService::resolveProfileRequest)
            .flatMap(Optional::stream)
            .map(binaryContentService::create)
            .map(dto -> binaryContentRepository.findById(dto.id()))
            .flatMap(Optional::stream)
            .toList()
        ).orElseGet(Collections::emptyList);

    Message message = new Message(messageCreateRequest.content(), channel, author, attachments);
    Message savedMessage = messageRepository.save(message);
    log.info("Message entity saved: id = {}", savedMessage.getId());

    return messageMapper.toDto(savedMessage);
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(() -> new MessageNotfoundException(
            ErrorCode.MESSAGE_NOT_FOUND,
            Map.of("messageId", messageId)
        ));
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    if (!channelRepository.existsById(channelId)) {
      throw new ChannelNotFoundException(
          ErrorCode.CHANNEL_NOT_FOUND,
          Map.of("channelId", channelId)
      );
    }

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
  @Transactional
  @PreAuthorize("@authz.isMessageOwner(#messageId, principal.user.id)")
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotfoundException(
            ErrorCode.MESSAGE_NOT_FOUND,
            Map.of("messageId", messageId)
        ));

    if (request.newContent() != null) {
      message.updateContent(request.newContent());
      log.info("Message entity updated - content changed: id = {}", message.getId());
    }

    return messageMapper.toDto(message);
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN') or @authz.isMessageOwner(#messageId, principal.user.id)")
  public void delete(UUID messageId) {
    if (!messageRepository.existsById(messageId)) {
      throw new MessageNotfoundException(
          ErrorCode.MESSAGE_NOT_FOUND,
          Map.of("messageId", messageId)
      );
    }
    messageRepository.deleteById(messageId);

    log.info("Message entity deleted: id = {}", messageId);
  }
}
