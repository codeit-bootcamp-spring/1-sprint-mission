package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> binaryContentRequests);
    MessageDto find(UUID messageId);
    PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt, Pageable pageable);
    MessageDto update(UUID messageId, MessageUpdateRequest request);
    void delete(UUID messageId);
}
