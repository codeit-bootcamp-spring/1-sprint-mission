package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  // 메시지 생성
  MessageDto create(CreateMessageRequest messageRequest,
      List<CreateBinaryContentRequest> binaryContentRequests);

  // 메시지 단건 조회
  MessageDto find(UUID messageId);

  // 메시지 다건 조회(페이징)
  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt, Pageable pageable);

  // 메시지 수정
  MessageDto update(UUID messageId, UpdateMessageRequest request);

  // 메시지 삭제
  void delete(UUID messageId);
}