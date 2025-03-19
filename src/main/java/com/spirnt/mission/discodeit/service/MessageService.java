package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageDto;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  // Create
  MessageDto create(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments); // 메세지 전송

  // Read
  MessageDto find(UUID messageId);  // 메세지 단건 조회

  PageResponse findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable); // 모든 메세지 조회

  // Update
  MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest);    // 메시지 수정

  // Delete
  void delete(UUID messageId);    // 메세지 삭제

}
