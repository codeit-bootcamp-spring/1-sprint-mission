package com.sprint.mission.service;


import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.entity.main.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
//    void create(MessageDtoForCreate responseDto, Optional<BinaryContentDto> attachmentsDto);

    Message create(MessageDtoForCreate responseDto, List<BinaryContentDtoForCreate> attachmentsDto);

    void update(UUID messageId, MessageDtoForUpdate updateDto);
    List<Message> findAllByChannelId(UUID channelId);
    //List<Message> findAll();
    void delete(UUID messageId);

    Message findById(UUID messageId);

    void deleteAllByChannelId(UUID channelId);
}
