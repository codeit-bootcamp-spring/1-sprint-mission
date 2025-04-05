package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.BusinessException;
import com.sprint.mission.discodeit.global.exception.binarycontent.FileConversionException;
import com.sprint.mission.discodeit.global.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
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
import java.util.HashMap;
import java.util.Map;
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

@Slf4j
@Service
@RequiredArgsConstructor
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

    UUID userId = request.userId();
    UUID channelId = request.channelId();

    User user = userRepository.findById(userId).orElseThrow(() ->
        new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)));

    Channel channel = channelRepository.findById(channelId).orElseThrow(() ->
        new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, Map.of("channelId", channelId)));

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

      log.info("Created message - id: {}", message.getId());
      return messageMapper.entityToDto(message);
    }
    return null;
  }

  @Override
  public PageResponse<MessageResponse> findAllByChannelId(UUID channelId) {
    channelRepository.findById(channelId).orElseThrow(() ->
        new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, Map.of("channelId", channelId)));

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

      log.info("Updated message - id: {}", message.getId());
      return messageMapper.entityToDto(message);
    }
    return null;
  }

  @Override
  public void deleteById(UUID id) {
    findByIdOrThrow(id);
    messageRepository.deleteById(id);
    log.info("Deleted message - id: {}", id);
  }

  private Message findByIdOrThrow(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(
            () -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND, Map.of("id", id)));
  }

  private byte[] convertToBytes(MultipartFile imageFile) {
    try {
      return imageFile.getBytes();
    } catch (IOException e) {
      throw new FileConversionException(ErrorCode.INTERNAL_SERVER_ERROR,
          Map.of("fileName", imageFile.getOriginalFilename()));
    }
  }
}
