package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;

import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;


import com.sprint.mission.discodeit.dto.reponse.PageResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  @Transactional
  MessageDto createMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests);

  // Read : 전체 메세지 조회, 특정 메세지 읽기
  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable);

  MessageDto getMessageById(UUID id);


  // Update : 특정 메세지 수정
  @Transactional
  MessageDto updateMessageText(UUID messageId, MessageUpdateRequest messageUpdateRequest);

  // Delete : 특정 메세지 삭제
  @Transactional
  void deleteMessageById(UUID id);
}
