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
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.file.FileUploadFailedException;
import com.sprint.mission.discodeit.exception.file.InvalidFileDataException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
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
  private final PageResponseMapper pageResponseMapper;


  @Override
  @Transactional
  public MessageDto createMessage(CreateMessageRequestDto request,
      List<BinaryContentDto> binaryRequests) {
    log.info("메시지 생성 요청: channelId={}, authorId={}, 첨부파일 수={}, 내용={}",
        request.getChannelId(), request.getAuthorId(), binaryRequests.size(), request.getContent());

    Channel channel = channelRepository.findById(request.getChannelId())
        .orElseThrow(() -> {
          log.warn("채널 없음: id={}", request.getChannelId());
          return new ChannelNotFoundException();
        });

    User author = userRepository.findById(request.getAuthorId())
        .orElseThrow(() -> {
          log.warn("작성자 없음: id={}", request.getAuthorId());
          return new UserNotFoundException();
        });

    List<UUID> attachmentIds = binaryRequests.stream()
        .map(binaryRequest -> {
          BinaryContent savedContent = binaryContentService.saveBinaryContent(binaryRequest);
          String extension = getFileExtension(binaryRequest.getFileName());
          binaryContentStorage.put(savedContent.getId(), binaryRequest.getBytes(), extension);
          log.debug("첨부파일 저장 완료: id={}, name={}", savedContent.getId(),
              binaryRequest.getFileName());
          return savedContent.getId();
        })
        .toList();

    Message message = new Message(request.getContent(), channel, author, attachmentIds);
    Message saved = messageRepository.save(message);

    log.info("메시지 생성 완료: id={}, authorId={}", saved.getId(), author.getId());
    return messageMapper.toDto(saved);
  }

  @Override
  public Message getMessageById(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(MessageNotFoundException::new);

  }

  @Override
  public List<Message> getAllMessages() {
    return messageRepository.findAll();
  }

  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor, int size) {
    Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    Slice<Message> messages;

    if (cursor == null) {
      messages = messageRepository.findFirstMessages(channelId, pageable);
    } else {
      messages = messageRepository.findNextMessages(channelId, cursor, pageable);
    }

    boolean hasNext = messages.getContent().size() == size;
    Instant nextCursor =
        hasNext ? messages.getContent().get(messages.getContent().size() - 1).getCreatedAt() : null;

    List<MessageDto> dtos = messages.stream()
        .map(messageMapper::toDto)
        .toList();

    Slice<MessageDto> dtoSlice = new SliceImpl<>(dtos, pageable, hasNext);

    return pageResponseMapper.fromSlice(dtoSlice, nextCursor);
  }


  @Override
  @Transactional
  public MessageDto updateMessage(UUID id, UpdateMessageRequestDto request) {
    log.info("메시지 수정 요청: id={}, newContent={}", id, request.getNewContent());

    Message message = messageRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("수정 대상 메시지 없음: id={}", id);
          return new MessageNotFoundException();
        });

    message.update(request.getNewContent());
    log.debug("메시지 내용 수정 완료");

    readStatusService.update(message.getId(), new UpdateReadStatusRequestDto(Instant.now()));
    log.debug("읽음 상태 업데이트 완료");

    return messageMapper.toDto(message);
  }


  @Override
  @Transactional
  public void deleteMessage(UUID id) {
    log.info("메시지 삭제 요청: id={}", id);

    if (!messageRepository.existsById(id)) {
      log.warn("삭제 대상 메시지 없음: id={}", id);
      throw new MessageNotFoundException();
    }

    messageRepository.deleteById(id);
    log.info("메시지 삭제 완료: id={}", id);
  }


  @Override
  @Transactional
  public void deleteByChannelId(UUID channelID) {
    messageRepository.deleteByChannelId(channelID);
  }

  @Override
  public BinaryContentDto saveAttachment(MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      throw new InvalidFileDataException();
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
      throw new FileUploadFailedException();
    }
    return binaryContentMapper.toDto(saveContent);
  }

  private String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex > 0) ? fileName.substring(dotIndex) : "";
  }
}
