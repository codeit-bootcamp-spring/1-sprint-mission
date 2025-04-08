package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.BinaryContentStoreDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicMessageService extends MessageMapper implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;
  // FileBinaryContentRepository에 @Repository 추가하니까 밑줄 사라짐 -> 해당 레포지토리 구현체가 빈으로 등록됐다는 말
  // 즉, 생성자 생성 -> 빈 등록 (@R.A.C, @Service), 의존성 주입 -> "빈 저장소에서 해당 레포지토리 타입으로 검색 후, 구현체 빈을 갖고와 주입해주는데 " -> FileBinaryContentRepository가 빈 저장소에 없었으니까 오류였던 것.

  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
                           List<BinaryContentStoreDto> attachments) {

    // 요청 파라미터 값 곧 생성할 객체 변수에 넣어주기
    UUID channelId = messageCreateRequest.getChannelId();
    UUID userId = messageCreateRequest.getAuthorId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(null));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(null));
    String content = messageCreateRequest.getContent();

    // 첨부파일 1. DB에 메타정보 저장 2. 로컬 저장소에 바이너리 데이터 저장
    List<BinaryContent> metaInfos = new ArrayList<>();
    for (BinaryContentStoreDto request : attachments) {
      String fileName = request.getFileName();
      String contentType = request.getContentType();
      int size = request.getSize();
      BinaryContent metaInfo = BinaryContent.builder()
              .fileName(fileName)
              .size(size)
              .contentType(contentType)
              .build();
      metaInfos.add(metaInfo);
      binaryContentRepository.save(metaInfo);

      byte[] data = request.getBytes();
      UUID id = request.getId();
      binaryContentStorage.put(id, data);
    }

    // 메세지 생성 -> DB에 저장
    Message message = Message.builder()
        .content(content)
        .channel(channel)
        .user(user)
        .attachments(metaInfos)
        .build();
    Message createdMessage = messageRepository.save(message);

    if (createdMessage != null) {
      MessageDto createdMessageDto = toDto(createdMessage);
      System.out.println("생성된 메세지: " + createdMessageDto);
      return createdMessageDto;
    } else {
      return null;
    }
  }

  @Override
  public Optional<Message> find(UUID messageId) {
    Optional<Message> msg = messageRepository.findById(messageId);
    msg.ifPresent(m -> System.out.println("조회된 메세지: " + m));
    return msg;
  }

  // 메세지 목록 조회
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
    Page<Message> messagePage = messageRepository.findAllByChannelId(channelId, pageable);
    Page<MessageDto> messagePageDto = messagePage.map(message -> messageMapper.toDto(message));
    return pageResponseMapper.fromPage(messagePageDto);
  }


  @Transactional
  @Override
  // 메세지 아이디를 전달했을 때, 그 메세지가 DB에 존재해야함 -> 메세지를 쓴 사람과 요청한 사람이 일치하는지 검사해야함 -> 일치하면 수정
  public MessageDto update(UUID messageId, UUID requesterId, MessageUpdateRequest request) {
    // DB에서 해당 메세지를 먼저 조회해야함 -> 예외 처리
    // mR.findById~ 는 Optional 타입이라 그냥 Message 타입인 .getAuthorId() 직접 호출이 안됨
    //  -> **.orElseThrow()로 null이 아닐 경우를 벗겨주고 Message 타입 변수에 넣어주는 작업
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException(null));

    if (!message.getUser().getId().equals(requesterId)) {
      System.out.println("메세지를 수정할 권한이 없습니다.");
    }
    String newContent = request.getNewContent();
    message.update(newContent);
    return toDto(message); // 전에는 toDto(messageRepository.save(message))였음.
  }


  @Transactional
  @Override
  // 메세지 아이디를 전달했을 때, 그 메세지가 DB에 존재해야함 -> 메세지를 쓴 사람과 요청한 사람이 일치하는지 검사 -> 일치하면 첨부파일 삭제 -> 메세지 삭제
  public void delete(UUID messageId, UUID requesterId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("메세지를 찾을 수 없습니다."));

    if (!message.getUser().getId().equals(requesterId)) {
      System.out.println("메세지를 삭제할 권한이 없습니다.");
    }
    binaryContentRepository.deleteById(messageId);
    messageRepository.deleteById(messageId);
  }
}