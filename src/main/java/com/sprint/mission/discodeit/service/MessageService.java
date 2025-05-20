package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.MessageRequest;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResponse createMessage(MessageRequest.Create request, List<MultipartFile> messageFiles);

    PageResponse<MessageResponse> findAllByChannelId(UUID channelId, Instant createdAt,
        Pageable pageable);

    MessageResponse findById(UUID id);

    MessageResponse update(UUID id, MessageRequest.Update request);

    void deleteById(UUID id);

}
