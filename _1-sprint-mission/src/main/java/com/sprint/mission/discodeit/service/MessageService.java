package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  Message create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests); //메시지 생성

  Message findById(UUID messageId); // 메시지 단일 조회

  List<Message> findAllByChannelId(UUID channelId); // 모든 메시지 조회

  Message update(UUID messageId, MessageUpdateRequest messageUpdateRequest); // 메시지 수정

  void delete(UUID messageId); //메시지 삭제
}
