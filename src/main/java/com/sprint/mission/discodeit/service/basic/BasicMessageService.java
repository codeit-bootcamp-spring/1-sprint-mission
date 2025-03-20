package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.message.MessageContentUpdateRequest;
import com.sprint.mission.discodeit.entity.message.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.message.MessageCreateResponse;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.ChannelAuthorNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.entitymapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.entitymapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.entitymapper.PageResponseMapper;
import com.sprint.mission.discodeit.mapper.entitymapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final PageResponseMapper<MessageDto> mapper = new PageResponseMapper<>();

  /**
   * 메세지 만들기
   */
  @Override
  public MessageDto create(MessageCreateRequest request,
      List<MultipartFile> attachments) {

    if (request.authorId() == null) {
      throw new ChannelAuthorNotFoundException(request.channelId().toString());
    }

    Channel findChannel = getChannel(request);
    User findUser = getUser(request);

    List<BinaryContent> files = convertToBinaryContent(attachments);
    Message message = createMessage(request, findChannel, findUser);
    message.attachFiles(files);
    messageRepository.save(message);

    uploadFiles(attachments, files);

    return MessageMapper.toDto(message);
  }


  /**
   * 커서 기반 페이징해서 메세지 가져오기
   */
  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findMessagesWithPaging(UUID channelId, Instant cursor,
      Pageable pageable) {

    PageRequest pageRequest = getPageRequest(pageable);

    if (cursor == null) {
      return getPaging(channelId, pageable);
    }

    return getCursorBasedPaging(channelId, cursor, pageable, pageRequest);
  }

  /**
   * 메세지 삭제
   */
  @Override
  public void remove(UUID messageId) {
    messageRepository.deleteById(messageId);
  }

  /**
   * 메세지 수정
   */
  @Override
  public MessageDto update(UUID messageId, MessageContentUpdateRequest request) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(MessageNotFoundException::new);

    message.updateContent(request.newContent());

    return MessageMapper.toDto(message);
  }

  /**
   * pageRequest 얻기
   */
  private static PageRequest getPageRequest(Pageable pageable) {
    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
        pageable.getSort());
  }

  // 파일 업로드
  private void uploadFiles(List<MultipartFile> attachments,
      List<BinaryContent> files) {
    if (attachments != null) {
      attachments.forEach(file -> files.forEach(bin -> {
            try {
              binaryContentStorage.put(bin.getId(), file.getBytes());
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          })
      );
    }
  }

  /**
   * DTO로 변환
   */
  private MessageCreateResponse convertMessageCreateResponse(List<BinaryContent> files,
      Message message) {
    List<BinaryContentDto> binaryContentDtos = files.stream()
        .map(BinaryContentMapper::toDto)
        .toList();

    return MessageCreateResponse.builder()
        .id(message.getId())
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .content(message.getContent())
        .channelId(message.getChannel().getId())
        .author(UserMapper.toDto(message.getAuthor()))
        .attachments(binaryContentDtos)
        .build();
  }

  private PageImpl<MessageDto> convertToMessageDto(Pageable pageable,
      Slice<Message> pagedMessages) {
    List<MessageDto> messageDtos = pagedMessages.stream()
        .map(MessageMapper::toDto)
        .toList();

    return new PageImpl<>(messageDtos, pageable, 0);
  }

  /**
   * multifile을 binaryContent로
   */
  private static List<BinaryContent> convertToBinaryContent(List<MultipartFile> attachments) {
    List<BinaryContent> files = null;
    if (attachments == null) {
      files = new ArrayList<>();
    } else {
      files = attachments.stream()
          .map(file -> new BinaryContent(file.getOriginalFilename(), file.getSize(),
              file.getContentType()))
          .toList();
    }
    return files;
  }

  private Message createMessage(MessageCreateRequest request, Channel findChannel, User findUser) {
    return Message.builder()
        .content(request.content())
        .channel(findChannel)
        .author(findUser)
        .build();
  }

  private User getUser(MessageCreateRequest request) {
    return userRepository.findById(request.authorId())
        .orElseThrow(UserNotFoundException::new);
  }

  private Channel getChannel(MessageCreateRequest request) {
    return channelRepository.findById(request.channelId())
        .orElseThrow(ChannelNotFoundException::new);
  }

  // 커서 기반 페이징
  private PageResponse<MessageDto> getCursorBasedPaging(UUID channelId, Instant cursor,
      Pageable pageable, PageRequest pageRequest) {

    Slice<Message> pagedMessages = messageRepository.findAllByChannelIdWithCursor(channelId,
        cursor, pageRequest);

    PageImpl<MessageDto> pages = convertToMessageDto(
        pageable, pagedMessages);

    return mapper.fromSlice(pages);
  }

  // 일반 페이징
  private PageResponse<MessageDto> getPaging(UUID channelId, Pageable pageable) {
    Page<Message> messages = messageRepository.findByChannel_Id(channelId, pageable);

    PageImpl<MessageDto> page = convertToMessageDto(pageable, messages);

    return mapper.fromPage(page);
  }

}
