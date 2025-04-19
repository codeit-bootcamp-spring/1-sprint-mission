package com.sprint.mission.service;


import com.sprint.mission.dto.response.MessageDto;
import com.sprint.mission.dto.response.PageResponse;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.dto.response.ScrollPageResponse;
import com.sprint.mission.entity.main.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageDtoForCreate responseDto, List<BinaryContentDtoForCreate> attachmentsDto);
    Message update(UUID messageId, MessageDtoForUpdate updateDto);
    List<PageResponse<MessageDto>> findAllByChannelId(UUID channelId, Pageable pageable);
    List<ScrollPageResponse<MessageDto>> findAllByChannelId(UUID channelId);
    void delete(UUID messageId);
    Message findById(UUID messageId);
    void deleteAllByChannelId(UUID channelId);
}
