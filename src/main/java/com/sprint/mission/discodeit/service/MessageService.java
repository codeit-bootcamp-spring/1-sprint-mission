package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.binarycontent.file.FileCreateException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@Transactional(readOnly = true)
public class MessageService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  @Transactional
  public MessageResponse createMessage(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments) {
    log.debug("createMessage() 호출");
    UUID authorId = messageCreateRequest.authorId();
    UUID channelId = messageCreateRequest.channelId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(Map.of("id", channelId)));
    User user = userRepository.findById(authorId)
        .orElseThrow(() -> new UserNotFoundException(Map.of("id", authorId)));

    if (channel.getType() == Channel.Type.PRIVATE) {
      if (!readStatusRepository.existsByUser_IdAndChannel_Id(authorId, channelId)) {
        throw new ReadStatusNotFoundException(Map.of("authorId", authorId, "channelId", channelId));
      }
    }

    List<BinaryContent> contents = new ArrayList<>();
    if (!(attachments == null || attachments.isEmpty())) {
      contents = attachments.stream()
          .map(attachment -> {
            long size = attachment.getSize();
            String fileName = attachment.getOriginalFilename();
            String contentType = fileName.substring(fileName.lastIndexOf('.'));

            BinaryContent content = binaryContentRepository
                .save(BinaryContent.create(size, fileName, contentType));
            try {
              binaryContentStorage.put(content.getId(), attachment.getBytes());
            } catch (IOException e) {
              throw new FileCreateException(Map.of());
            }
            return content;
          })
          .toList();
    }

    Message message = messageRepository
        .save(Message.create(user, messageCreateRequest.content(), channel, contents));
    log.info("Message 생성. id: {}", message.getId());
    return messageMapper.toDto(message);
  }

  public MessageResponse find(UUID id) {
    return messageRepository.findById(id)
        .map(messageMapper::toDto)
        .orElseThrow(() -> new MessageNotFoundException(Map.of("id", id)));
  }

  public PageResponse<MessageResponse> readAllByChannelId(
      UUID channelId, Instant cursor, Pageable pageable
  ) {
    log.debug("readAllByChannelId() 호출");
    Slice<MessageResponse> slice;
    if (cursor == null) {
      slice = messageRepository.findPageByChannelId(channelId, pageable)
          .map(messageMapper::toDto);
    } else {
      slice = messageRepository
          .findPageByChannelIdWithCursor(channelId, cursor, pageable)
          .map(messageMapper::toDto);
    }
    return pageResponseMapper.fromMessageResponse(slice);
  }

  @PreAuthorize("principal.user.id == @messageService.find(#messageId).author.id")
  @Transactional
  public MessageResponse updateMessage(UUID messageId, String content) {
    log.debug("updateMessage() 호출");
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException(Map.of("id", messageId)));
    message.updateContent(content);

    log.info("Message 수정. id: {}", messageId);
    messageRepository.save(message);
    return messageMapper.toDto(message);
  }

  @PreAuthorize("hasRole('ADMIN') or principal.user.id == @messageService.find(#messageId).author.id")
  @Transactional
  public void deleteMessage(UUID messageId) {
    messageRepository.findByIdWithAttachments(messageId)
        .ifPresent(message -> {
          message.getAttachments()
              .forEach(attachment -> binaryContentStorage.delete(attachment.getId()));
          log.info("Message 삭제. id: {}", messageId);
          messageRepository.delete(message);
        });
  }
}