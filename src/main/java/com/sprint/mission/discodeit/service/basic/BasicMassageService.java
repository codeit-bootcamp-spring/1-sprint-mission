package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.service.Interface.MessageService;
import com.sprint.mission.discodeit.service.Interface.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMassageService implements MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusService readStatusService;


  @Override
  public Message createMessage(CreateMessageRequestDto request,
      List<BinaryContentCreateRequestDto> binaryRequests) {
    if (!channelRepository.existsById(request.getChannelId())) {
      throw new NoSuchElementException("Channel not found");
    }
    if (!userRepository.existsById(request.getAuthorId())) {
      throw new NoSuchElementException("User not found");
    }

    List<UUID> attachmentIds = binaryRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.getFileName();
          String contentType = attachmentRequest.getContentType();
          byte[] bytes = attachmentRequest.getBytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
          return createdBinaryContent.getId();
        })
        .toList();
    Message message = new Message(request.getContent(), request.getChannelId(),
        request.getAuthorId(), attachmentIds);
    Message savedMessage = messageRepository.save(message);
    return savedMessage;
  }

  @Override
  public Message getMessageById(UUID id) {
    return messageRepository.getMessageById(id)
        .orElseThrow(() -> new NoSuchElementException("Message with id " + id + " not fount"));

  }

  @Override
  public List<Message> getAllMessages() {
    return messageRepository.getAllMessage();
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.getMessagesByChannelId(channelId).stream()
        .toList();
  }

  @Override
  public Message updateMessage(UUID id, UpdateMessageRequestDto request) {
    Message message = messageRepository.getMessageById(id)
        .orElseThrow(() -> new NoSuchElementException("Message with id " + id + " not found"));
    message.update(request.getNewContent());
    Instant now = Instant.now();
    UpdateReadStatusRequestDto updateReadStatus = new UpdateReadStatusRequestDto(now);
    readStatusService.update(id, updateReadStatus);
    return messageRepository.save(message);
  }


  @Override
  public void deleteMessage(UUID id) {
    if (!messageRepository.existsById(id)) {
      throw new NoSuchElementException("Message with id " + id + " not fount");
    }
    binaryContentService.deleteByMessageId(id);
    messageRepository.deleteMessage(id);
  }

  @Override
  public void deleteByChannelId(UUID channelID) {
    messageRepository.deleteByChannelId(channelID);
  }

}
