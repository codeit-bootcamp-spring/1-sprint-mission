package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service // 서비스 Bean
@RequiredArgsConstructor // 생성자
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final PageResponseMapper pageResponseMapper;

  //@Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    validateIds(channelId, authorId);

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(NoSuchElementException::new);
    User user = userRepository.findById(authorId).orElseThrow(NoSuchElementException::new);

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(b -> {
          UUID attachmentId = UUID.randomUUID();
          binaryContentStorage.put(attachmentId, b.bytes());
          return new BinaryContent(b.fileName(), (long) b.bytes().length, b.contentType());
        })
        .toList();

    Message message = new Message(
        messageCreateRequest.content(),
        channel,
        user,
        attachments
    );
    Message savedMessage = messageRepository.save(message);
    return messageMapper.toDto(savedMessage);
  }

  @Override
  public MessageDto findById(UUID messageId) {
    Message findedMessage = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    return messageMapper.toDto(findedMessage);
  }

  @Override
  public Page<Message> findAllByChannelId(UUID channelId, Pageable pageable) {
    //50개씩 최신순으로 정렬
    Pageable fixedPageable = PageRequest.of(pageable.getPageNumber(), 50,
        Sort.by("createdAt").descending());
    // Repository에서 모든 메시지를 가져오고, 페이징 적용
    List<Message> messages = messageRepository.findAllByChannelId(channelId);
    //메시지를 페이지로 변환
    int start = (int) fixedPageable.getOffset(); //현재 페이지에서 데이터를 가져올 때의 시작점
    int end = Math.min((start + fixedPageable.getPageSize()), messages.size()); //가져올 마지막 인덱스
    //기존 messages 리스트에서 start부터 end까지의 메시지만 추출하여 새로운 리스트 생성
    List<Message> pagedMessages = messages.subList(start, end);
    //PageImpl을 사용하여 List<Message>를 Page<Message> 객체로 변환
    return new PageImpl<>(pagedMessages, fixedPageable, messages.size());
  }

  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest updateMessageDto) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.updateContent(updateMessageDto.newContent());
    Message savedMessage = messageRepository.save(message);
    return messageMapper.toDto(savedMessage);
  }


  //@Transactional
  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    //관련된 BinaryContent 삭제
    message.getAttachments()
        .forEach(binaryContent -> binaryContentRepository.deleteById(binaryContent.getId()));
    //메시지 삭제
    messageRepository.deleteById(messageId);
  }

  public void validateIds(UUID channelId, UUID authorId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
    }
    if (!userRepository.existsById(authorId)) {
      throw new NoSuchElementException("Author with id " + authorId + " does not exist");
    }
  }
}
