package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  // 메시지 생성
  MessageDto create(MessageCreateRequest messageRequest,
      List<BinaryContentCreateRequest> binaryContentRequests);

  // 메시지 단건 조회
  MessageDto find(UUID messageId);

  // 메시지 다건 조회(페이징)
  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt, Pageable pageable);

  // 메시지 수정
  MessageDto update(UUID messageId, MessageUpdateRequest request);

  // 메시지 삭제
  void delete(UUID messageId);
}