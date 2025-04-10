package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserRepository userRepository;
  private final ChannelService channelService;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments) {
    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));

    User writer = userRepository.findById(messageCreateRequest.writerId())
        .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));

    if (channel.getType() == ChannelType.PRIVATE) {
      boolean isMember = channelService.getParticipantIds(channel.getId()).stream()
          .anyMatch(uuid -> uuid.equals(messageCreateRequest.writerId()));
      if (!isMember) {
        throw new IllegalArgumentException("PRIVATE 채널의 멤버가 아닙니다.");
      }
    }

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
                throw new RuntimeException("파일 처리 중 오류 발생", e);
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


  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));

    if (!message.getWriter().getId().equals(messageUpdateRequest.writerId())) {
      throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
    }

    message.update(messageUpdateRequest.newContent());
    messageRepository.save(message);
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId, UUID writerId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));

    if (!message.getWriter().getId().equals(writerId)) {
      throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다.");
    }
    messageRepository.deleteById(messageId);
  }
}
