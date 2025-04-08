package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.message.MessageContentUpdateRequest;
import com.sprint.mission.discodeit.entity.message.MessageCreateRequest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  /**
   * 스프린트 미션 6 message 생성
   */
  MessageDto create(MessageCreateRequest request, List<MultipartFile> attachments);

  //PageResponse<Message> findMessagesWithPaging(UUID channelId, Pageable pageable);

  PageResponse<MessageDto> findMessagesWithPaging(UUID channelId, Instant cursor,
      Pageable pageable);

  void remove(UUID messageId);

  MessageDto update(UUID messageId, MessageContentUpdateRequest request);

}
