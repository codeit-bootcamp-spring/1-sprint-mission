package com.sprint.mission.service;


import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.entity.main.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
//    void create(MessageDtoForCreate responseDto, Optional<BinaryContentDto> attachmentsDto);

    void create(MessageDtoForCreate responseDto, Optional<List<BinaryContentDto>> attachmentsDto);

    void update(UUID messageId, MessageDtoForUpdate updateDto);
    List<Message> findAllByChannelId(UUID channelId);
    //List<Message> findAll();
    void delete(UUID messageId);

    Message findById(UUID messageId);
}
