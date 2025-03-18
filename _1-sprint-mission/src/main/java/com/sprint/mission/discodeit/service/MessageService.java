package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests); //메시지 생성

  MessageDto findById(UUID messageId); // 메시지 단일 조회

  Page<Message> findAllByChannelId(UUID channelId, Pageable pageable); // 모든 메시지 조회

  MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest); // 메시지 수정

  void delete(UUID messageId); //메시지 삭제

}
