package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.service.Interface.MessageService;
import com.sprint.mission.discodeit.service.Interface.ReadStatusService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.nio.file.Path;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.*;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusService readStatusService;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;


  @Override
  @Transactional
  public MessageDto createMessage(CreateMessageRequestDto request,
      List<BinaryContentDto> binaryRequests) {
    Channel channel = channelRepository.findById(request.getChannelId())
        .orElseThrow(() -> new NoSuchElementException("channel not found"));

    User author = userRepository.findById(request.getAuthorId())
        .orElseThrow(() -> new NoSuchElementException("author not found"));

    List<UUID> attachmentIds = binaryRequests.stream()
        .map(binaryRequest -> {
          BinaryContent savedContent = binaryContentService.saveBinaryContent(binaryRequest);
          String extension = getFileExtension(binaryRequest.getFileName());
          binaryContentStorage.put(savedContent.getId(), binaryRequest.getBytes(), extension);
          return savedContent.getId();
        })
        .toList();

    Message message = new Message(request.getContent(), channel,
        author, attachmentIds);
    return messageMapper.toDto(messageRepository.save(message));
  }

  private String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex > 0) ? fileName.substring(dotIndex) : "";
  }

  @Override
  public Message getMessageById(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Message with id " + id + " not fount"));

  }

  @Override
  public List<Message> getAllMessages() {
    return messageRepository.findAll();
  }

  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor, int size) {
    Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<Message> messages;
    Long totalElements = null;
    //messageRepository.countByChannelId(channelId);

    if (cursor == null) {
      messages = messageRepository.findFirstMessages(channelId, pageable);
    } else {
      messages = messageRepository.findNextMessages(channelId, cursor, pageable);
    }

    boolean hasNext = messages.getContent().size() == size;
    Instant nextCursor =
        hasNext ? messages.getContent().get(messages.getContent().size() - 1).getCreatedAt() : null;

    return new PageResponse<>(messages.stream()
        .map(messageMapper::toDto)
        .collect(Collectors.toList()), nextCursor, size, hasNext, totalElements);
  }


  @Override
  @Transactional
  public MessageDto updateMessage(UUID id, UpdateMessageRequestDto request) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Message with id " + id + " not found"));
    message.update(request.getNewContent());
    Instant now = Instant.now();
    UpdateReadStatusRequestDto updateReadStatus = new UpdateReadStatusRequestDto(now);
    readStatusService.update(message.getId(), updateReadStatus);
    return messageMapper.toDto(message);
  }


  @Override
  @Transactional
  public void deleteMessage(UUID id) {
    if (!messageRepository.existsById(id)) {
      throw new NoSuchElementException("Message with id " + id + " not found");
    }
    messageRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void deleteByChannelId(UUID channelID) {
    messageRepository.deleteByChannelId(channelID);
  }

  @Override
  public BinaryContentDto saveAttachment(MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      throw new NoSuchElementException("multipartFile is null or empty");
    }
    BinaryContent binaryContent = new BinaryContent(
        multipartFile.getOriginalFilename(),
        multipartFile.getSize(),
        multipartFile.getContentType()
    );
    BinaryContent saveContent = binaryContentRepository.save(binaryContent);

    String extension = getFileExtension(multipartFile.getOriginalFilename());
    try {
      Path filePath = binaryContentStorage.put(saveContent.getId(), multipartFile.getBytes(),
          extension);
      binaryContent.setFilePath(filePath.toString());
    } catch (Exception e) {
      throw new RuntimeException("save binary content failed");
    }
    return binaryContentMapper.toDto(saveContent);
  }
}
