package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageDto;
import com.sprint.mission.discodeit.dto.messageDto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.messageDto.PagedResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PagedResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageMapper messageMapper;
  private final PagedResponseMapper pagedResponseMapper;

  public MessageDto create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new NoSuchElementException("Channel not found."));
    User author = userRepository.findById(request.authorId())
        .orElseThrow(() -> new NoSuchElementException("User not found."));

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(binaryContentCreateRequest -> new BinaryContent(
            binaryContentCreateRequest.fileName(),
            (long) binaryContentCreateRequest.bytes().length,
            binaryContentCreateRequest.contentType()
        ))
        .toList();
    binaryContentRepository.saveAll(attachments);
    for (int i = 0; i < attachments.size(); i++) {
      binaryContentStorage.put(attachments.get(i).getId(),
          binaryContentCreateRequests.get(i).bytes());
    }

    String content = request.content();
    Message message = new Message(content, channel, author, attachments);
    messageRepository.save(message);
    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  public MessageDto findById(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("Message not found: " + messageId));
  }

  @Transactional(readOnly = true)
  public PagedResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
    Page<MessageDto> messagePage = messageRepository.findAllByChannelId(channelId, pageable)
        .map(messageMapper::toDto);

    return pagedResponseMapper.toPagedResponse(messagePage);
  }

  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    messageRepository.save(message);
    return messageMapper.toDto(message);
  }

  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    binaryContentRepository.deleteAll(message.getAttachments());
    messageRepository.deleteById(messageId);
  }
}
