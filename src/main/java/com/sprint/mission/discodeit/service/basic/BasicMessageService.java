package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션

public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  private final MessageMapper messageMapper;

  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public MessageDto createMessage(MessageCreateDTO messageCreateDTO,
      List<BinaryContentCreateDTO> attachmentRequests) {
    Channel foundChannel = channelRepository.findById(messageCreateDTO.channelId()).orElseThrow(()
        -> new NoSuchElementException(messageCreateDTO.channelId() + "does not exist"));

    User foundUser = userRepository.findById(messageCreateDTO.userId()).orElseThrow(()
        -> new NoSuchElementException(messageCreateDTO.userId() + "does not exist"));

    List<BinaryContent> attachments = attachmentRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .toList();

    //builder를 통한 message만들기
    Message message = Message.builder()
        .content(messageCreateDTO.content())
        .channel(foundChannel)
        .author(foundUser)
        .attachments(messageCreateDTO.attachments()).build();

    return messageMapper.toDto(messageRepository.save(message));
  }

  @Override
  public MessageDto findById(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Message not found"));
    return messageMapper.toDto(message);
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    return messageRepository.findByChannelId(channelId)
        .stream()
        .map(messageMapper::toDto)
        .collect(Collectors.toList());
  }


  @Override
  public MessageDto update(UUID id, MessageUpdateDTO messageUpdateDTO) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Message not found"));

    message.updateContent(messageUpdateDTO.content());
    return messageMapper.toDto(messageRepository.save(message));
  }

  @Override
  public void deleteMessage(UUID msgID) {
    messageRepository.deleteById(msgID);
  }
}
