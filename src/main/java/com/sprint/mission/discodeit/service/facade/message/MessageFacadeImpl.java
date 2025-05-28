package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.mapper.MessageMapper;

import com.sprint.mission.discodeit.service.basic.PermissionService;

import com.sprint.mission.discodeit.service.message.MessageManagementService;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageFacadeImpl implements MessageFacade {

  private final MessageManagementService messageManagementService;
  private final MessageMapper messageMapper;

  private final PermissionService permissionService;


  @Override
  @Transactional
  public MessageResponseDto createMessage(CreateMessageDto messageDto, List<MultipartFile> files) {
    Message message = messageManagementService.createMessage(messageDto.content(),
        messageDto.channelId(), messageDto.authorId(), files);
    log.debug("[MESSAGE SENT] : [ID : {}]", message.getId());
    return messageMapper.toResponseDto(message);
  }

  @Override
  @Transactional(readOnly = true)
  public MessageResponseDto findMessageById(String id) {
    Message message = messageManagementService.findSingleMessage(id);
    return messageMapper.toResponseDto(message);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageResponseDto> findMessagesByChannel(String channelId,
      Instant nextCursor, Pageable pageable) {

    if (channelId.isBlank()) {
      throw new DiscodeitException(ErrorCode.INVALID_UUID_FORMAT, Map.of("channelId", channelId));
    }

    Page<Message> messagePage = messageManagementService.findMessagesByChannel(channelId,
        nextCursor, pageable);

    List<MessageResponseDto> dtoList = messageMapper.fromEntityList(messagePage.getContent());

    Instant newCursor = dtoList.isEmpty() ? null : dtoList.get(dtoList.size() - 1).createdAt();
    return new PageResponse<>(
        dtoList,
        newCursor,
        pageable.getPageSize(),
        messagePage.hasNext(),
        null
    );
  }

  @Override
  @Transactional
  public MessageResponseDto updateMessage(String messageId, MessageUpdateDto messageDto,
      UserDetails userDetails) {

    if (!permissionService.checkIsAuthor(userDetails, UUID.fromString(messageId))) {
      throw new DiscodeitException(ErrorCode.ACCESS_DENIED);
    }

    Message message = messageManagementService.updateMessage(messageId, messageDto.newContent());
    return messageMapper.toResponseDto(message);
  }

  @Override
  @Transactional
  public void deleteMessage(String messageId,
      UserDetails userDetails) {
    permissionService.checkIsAdminOrAuthor(UUID.fromString(messageId), userDetails);
    messageManagementService.deleteMessage(messageId);
  }

}
