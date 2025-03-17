package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.Cursor;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponceMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentService BasicBinaryContentService;
  private final MessageMapper messageMapper;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
    }
    if (!userRepository.existsById(authorId)) {
      throw new NoSuchElementException("Author with id " + authorId + " does not exist");
    }

    List<UUID> attachmentIds = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          BinaryContentDto createdBinaryContent = BasicBinaryContentService.create(attachmentRequest);
          return createdBinaryContent.id();
        })
        .toList();

    String content = messageCreateRequest.content();
    Message message = new Message(
        content,
        channelId,
        authorId,
        attachmentIds
    );
    return messageMapper.messageToDto(messageRepository.save(message));
  }

  @Override
  public MessageDto find(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

    return messageMapper.messageToDto(message);
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream()
        .map(messageMapper::messageToDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    return  messageMapper.messageToDto(messageRepository.save(message));
  }


  @Override
  @Transactional
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    message.getAttachments()
        .forEach(BasicBinaryContentService::delete);

    messageRepository.deleteById(messageId);
  }

  @Override
  @Transactional
  public PageResponse<MessageDto> pageMessage(int start,int end){
    Pageable pageable = PageRequest.of(start, end);
    Page<Message> messages = messageRepository.findTopNByOrderByCreatedAtAsc(pageable);
    Page<MessageDto> messageDtoPage = messages.map(messageMapper::messageToDto);
    return PageResponceMapper.fromPage(messageDtoPage);
  }

  @Override
  @Transactional
  public PageResponse<MessageDto> cursorMessage(UUID lastMessageId, int size) {
    Page<Message> messages;
    if (lastMessageId == null) {
      Pageable pageable = PageRequest.of(0, size);
      messages = messageRepository.findTopNByOrderByCreatedAtAsc(pageable);
    } else {
      messages = messageRepository.findByIdGreaterThanOrderByCreatedAt(lastMessageId, PageRequest.of(0, size));
    }
    List<MessageDto> dtoList = messages.getContent().stream()
        .map(messageMapper::messageToDto)
        .collect(Collectors.toList());
    UUID nextCursor = dtoList.isEmpty() ? null : dtoList.get(dtoList.size() - 1).id();
    Cursor<MessageDto> cursorPage = new Cursor<>(dtoList, nextCursor, !dtoList.isEmpty());
    return PageResponceMapper.fromCursor(cursorPage, size);
  }

}

