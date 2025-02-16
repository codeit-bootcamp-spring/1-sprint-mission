package com.sprint.mission.service;


import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(MessageDtoForCreate responseDto);
    void update(MessageDtoForUpdate updateDt);
    List<Message> findAllByChannelId(UUID channelId);
    //List<Message> findAll();
    void delete(UUID messageId);

    Message findById(UUID messageId);
}
