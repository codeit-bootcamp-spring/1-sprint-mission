package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentService binaryContentService;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " does not exist"));
    User author = userRepository.findById(authorId)
        .orElseThrow(
            () -> new NoSuchElementException("Author with id " + authorId + " does not exist"));

    String content = messageCreateRequest.content();
    Message message = new Message(content, channel, author, new ArrayList<>());
    message = messageRepository.save(message);

    for (BinaryContentCreateRequest attachmentRequest : binaryContentCreateRequests) {
      BinaryContentDto binaryContentDto = binaryContentService.create(attachmentRequest);

      MessageAttachment attachment = new MessageAttachment(message, binaryContentDto.getId());
      message.addAttachment(attachment);
    }

    Message savedMessage = messageRepository.save(message);
    return toDto(savedMessage);
  }

  @Override
  @Transactional
  public MessageDto find(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    return toDto(message);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, String cursor, int size,
      String sort) {
    String[] sortParams = sort.split(",");
    String sortField = sortParams[0];
    String sortDir = sortParams.length > 1 ? sortParams[1] : "desc";
    Sort.Direction direction = Sort.Direction.fromString(sortDir);
    Sort sortOrder = Sort.by(direction, sortField);

    Pageable pageable = PageRequest.of(0, size, sortOrder);
    List<Message> messages;

    if (cursor == null) {
      messages = messageRepository.findAllByChannel_Id(channelId, pageable).getContent();
    } else {
      messages = messageRepository.findByChannelIdAndCursor(channelId, cursor, pageable);
    }

    // 첨부 파일 조회 및 MessageDto 변환 로직 (기존과 동일)
    List<UUID> attachmentIds = messages.stream()
        .flatMap(msg -> msg.getAttachments().stream().map(MessageAttachment::getAttachmentId))
        .distinct()
        .collect(Collectors.toList());

    List<BinaryContentDto> attachmentDtos = attachmentIds.isEmpty() ?
        new ArrayList<>() : binaryContentService.findAllByIdIn(attachmentIds);

    List<MessageDto> messageDtos = messages.stream().map(message -> {
      UserDto authorDto = new UserDto(
          message.getAuthor().getId(),
          message.getAuthor().getUsername(),
          message.getAuthor().getEmail(),
          message.getAuthor().getProfile() != null ?
              new BinaryContentDto(
                  message.getAuthor().getProfile().getId(),
                  message.getAuthor().getProfile().getFileName(),
                  message.getAuthor().getProfile().getSize(),
                  message.getAuthor().getProfile().getContentType()
              ) : null,
          null
      );

      List<BinaryContentDto> messageAttachments = attachmentDtos.stream()
          .filter(attachment -> message.getAttachments().stream()
              .anyMatch(msgAtt -> msgAtt.getAttachmentId().equals(attachment.getId())))
          .collect(Collectors.toList());

      return new MessageDto(
          message.getId(),
          message.getCreatedAt(),
          message.getUpdatedAt(),
          message.getContent(),
          message.getChannel().getId(),
          authorDto,
          messageAttachments
      );
    }).collect(Collectors.toList());

    // 다음 커서 계산
    String nextCursor = messages.isEmpty() ? null :
        messages.get(messages.size() - 1).getCreatedAt().toString();
    boolean hasNext = messages.size() == size;

    // PageResponseMapper를 사용해 응답 생성
    return PageResponseMapper.toCursorPageResponse(messageDtos, size, hasNext, nextCursor);
  }


  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    Message updatedMessage = messageRepository.save(message);
    return toDto(updatedMessage);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    message.getAttachments().forEach(attachment -> {
      binaryContentService.delete(attachment.getAttachmentId());
    });

    messageRepository.deleteById(messageId);
  }

  private MessageDto toDto(Message message) {
    UserDto authorDto = new UserDto(
        message.getAuthor().getId(),
        message.getAuthor().getUsername(),
        message.getAuthor().getEmail(),
        null,
        null
    );

    List<BinaryContentDto> attachmentDtos = message.getAttachments().stream()
        .map(attachment -> binaryContentService.find(attachment.getAttachmentId()))
        .collect(Collectors.toList());

    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        authorDto,
        attachmentDtos
    );
  }
}