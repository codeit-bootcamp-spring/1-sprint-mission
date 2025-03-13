package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.reponse.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
//
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  //
  private final BinaryContentService binaryContentService;
  //
  private final MessageMapper messageMapper;
  private final BinaryContentMapper binaryContentMapper;
  //
  private final InputHandler inputHandler;
  private final PageResponseMapper<MessageDto> pageResponseMapper; // 제네릭 타입 명시 안해주면 Raw Type을 쓰고 있다고 경고를 준다.

  @Transactional
  @Override
  public MessageDto createMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {

    if (channelRepository.findById(messageCreateRequest.channelId()).isEmpty()) {
      throw new IllegalArgumentException(
          "채널( " + messageCreateRequest.channelId() + " )이 존재하지 않습니다.");
    }
    if (userRepository.findById(messageCreateRequest.authorId()).isEmpty()) {
      throw new IllegalArgumentException(
          "유저(" + messageCreateRequest.authorId() + ") 존재하지 않습니다.");
    }

    List<BinaryContent> binaryContents = binaryContentCreateRequests.stream()
        .map(binaryContentService::createBinaryContent)
        .map(binaryContentMapper::toEntity)
        .toList();

    Message message = Message.builder()
        .content(messageCreateRequest.content())
        .attachments(
            binaryContents
        )
        .author(
            userRepository.findById(messageCreateRequest.authorId())
                .orElseThrow(() -> new NoSuchElementException(
                    "유저(" + messageCreateRequest.authorId() + ") 존재하지 않습니다."))
        )
        .channel(
            channelRepository.findById(messageCreateRequest.channelId())
                .orElseThrow(() -> new NoSuchElementException(
                    "채널(" + messageCreateRequest.channelId() + ")이 존재하지 않습니다."))
        )
        .build();

    messageRepository.save(message);

    return messageMapper.toDto(message);
  }

  // 페이징 처리가 필요
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
    // 왜 이렇게 변환하는거지 (이해가 필요...)

    // 페이징된 데이터 조회
    Page<Message> messagePage = messageRepository.findByChannelId(channelId, pageable);

    // Message -> MessageDto, Page<T> 인터페이스가 기본적으로 map(Function<T, R> converter) 메서드 제공
    Page<MessageDto> messageDtoPage = messagePage.map(messageMapper::toDto);

    // Page -> PageResponse<MessageDto> 필요한 정보만 담기
    return pageResponseMapper.fromPage(messageDtoPage);
  }

  @Override
  public MessageDto getMessageById(UUID id) {
    Message message =
        messageRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 메세지가 없습니다."));
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public MessageDto updateMessageText(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("메세지(" + messageId + " )가 존재하지 않습니다."));

    message.updateMessageText(messageUpdateRequest.newMessage());
    message.refreshUpdateAt();

    // JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void deleteMessageById(UUID id) {
    String keyword = inputHandler.getYesNOInput();
    if (keyword.equalsIgnoreCase("y")) {

      if (messageRepository.findById(id).isEmpty()) {
        throw new NoSuchElementException("메세지(" + id + ")를 찾을 수 없습니다.");
      }

      messageRepository.findById(id).stream()
          .map(Message::getAttachments)
          .flatMap(List::stream) // List에서 하나씩
          .map(BaseEntity::getId)
          .forEach(binaryContentService::deleteBinaryContentById);

      messageRepository.deleteById(id);
    }
  }
}
