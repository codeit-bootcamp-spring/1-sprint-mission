package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;
  private final ReadStatusService readStatusService;

  @Override
  public Message create(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments) {
    // User와 Channel이 존재하는지 검증
    UUID userId = messageCreateRequest.getAuthorId();
    UUID channelId = messageCreateRequest.getChannelId();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(
            "Channel with id " + messageCreateRequest.getChannelId() + " not found"));
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException(
          "User with id " + messageCreateRequest.getAuthorId() + " not found");
    }
    // 스펙에 명시되지 않은 예외 주석 처리
//    // PRIVATE 채널인데 입장하지 않은 유저가 메시지를 쓰려고 하는 경우
//    if (channel.getType().equals(ChannelType.PRIVATE)
//        && !readStatusService.existsByUserIdChannelId(userId, channelId)) {
//      throw new IllegalArgumentException("User did not joined this private channel");
//    }
    // 첨부 파일 업로드
    List<UUID> attachedFilesId = new ArrayList<>();
    for (MultipartFile file : Optional.ofNullable(attachments)
        .orElse(Collections.emptyList())) { // null 방지 optional 사용
      BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(file);
      BinaryContent binaryContent = binaryContentService.create(binaryContentCreateRequest);
      attachedFilesId.add(binaryContent.getId());
    }
    Message message = new Message(messageCreateRequest.getContent(),
        channelId,
        userId,
        attachedFilesId);
    messageRepository.save(message);
    // 메세지 작성자를 Online 상태로
    userStatusService.updateByUserId(userId, new UserStatusUpdateRequest(Instant.now()));
    return message;
  }

  @Override
  public Message find(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    return message;
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " not found");
    }

    Map<UUID, Message> data = messageRepository.findAll();
    return data.values().stream()
//                .filter(message -> message.getChannelId()==channelId)
        /* 왜 ==이 안 됐을까? ==은 메모리 주소가 같은지 확인하는 연산자이고
         *  Message 객체 속 값과 클라이언트로부터 받아온 값은 주소가 같을 수 없으니까
         * 내용의 동등성을 확인하는 equals()가 적절함 */
        .filter(message -> message.getChannelId().equals(channelId))
        .sorted(Comparator.comparing(message -> message.getCreatedAt()))
        .collect(Collectors.toList());
  }


  @Override
  public Message update(UUID messageId, MessageUpdateRequest dto) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(dto.getNewContent());
    messageRepository.save(message);
    return message;
  }

  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    // 첨푸 파일 삭제
    List<UUID> attachedFiles = message.getAttachmentIds();
    for (UUID id : attachedFiles) {
      binaryContentService.delete(id);
    }
    messageRepository.delete(messageId);
  }

}
