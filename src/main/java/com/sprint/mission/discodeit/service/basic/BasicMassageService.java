package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.validation.MessageValidator;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicMassageService implements MessageService {

  private final MessageRepository messageRepository;
  private final MessageValidator messageValidator;
  private final MessageMapper messageMapper;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public MessageResponse createMessage(MessageRequest.Create request,
      List<MultipartFile> messageFiles) {

    User user = userRepository.findById(request.userId()).orElseThrow(() ->
        new RestApiException(ErrorCode.USER_NOT_FOUND, "userId : " + request.userId()));
    Channel channel = channelRepository.findById(request.channelId()).orElseThrow(() ->
        new RestApiException(ErrorCode.CHANNEL_NOT_FOUND, "channelId : " + request.channelId()));

    if (messageValidator.inValidContent(request.content())) {
      Message message = Message.createMessage(request.content(), channel, user);
      Optional.ofNullable(messageFiles).ifPresent(files ->
          files.forEach(file -> {
                BinaryContent binaryContent = binaryContentRepository.save(
                    BinaryContent.createBinaryContent(
                        file.getOriginalFilename(),
                        file.getSize(),
                        file.getContentType()));
                binaryContentStorage.put(binaryContent.getId(), convertToBytes(file));
                message.insertAttachments(binaryContent);
              }
          )
      );
      messageRepository.save(message);

      log.info("Create Message: {}", message);
      return messageMapper.entityToDto(message);
    }
    return null;
  }

  @Override
  public PageResponse<MessageResponse> findAllByChannelId(UUID channelId) {
    channelRepository.findById(channelId).orElseThrow(() ->
        new RestApiException(ErrorCode.CHANNEL_NOT_FOUND, "id : " + channelId));

    Pageable pageable = PageRequest.of(0, 50, Sort.by("createdAt").descending());
    Slice<Message> slice = messageRepository.findAllByChannelId(channelId, pageable);
    Slice<MessageResponse> responseSlice = slice.map(messageMapper::entityToDto);
    
    return PageResponseMapper.fromSlice(responseSlice);
  }

  @Override
  public MessageResponse findById(UUID id) {
    return messageMapper.entityToDto(findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public MessageResponse update(UUID id, MessageRequest.Update request) {
    Message message = findByIdOrThrow(id);
    if (messageValidator.inValidContent(request.newContent())) {
      message.updateContent(request.newContent());
      messageRepository.save(message);

      log.info("update message: {}", message);
      return messageMapper.entityToDto(message);
    }
    return null;
  }

  @Override
  public void deleteById(UUID id) {
    messageRepository.deleteById(id);
  }

  private Message findByIdOrThrow(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(() -> new RestApiException(ErrorCode.MESSAGE_NOT_FOUND, "id : " + id));
  }

  private byte[] convertToBytes(MultipartFile imageFile) {
    try {
      return imageFile.getBytes();
    } catch (IOException e) {
      throw new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR, "변환 실패");
    }
  }
}
