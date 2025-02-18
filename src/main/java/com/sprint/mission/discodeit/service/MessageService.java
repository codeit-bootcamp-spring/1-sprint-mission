package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;


import java.util.List;
import java.util.UUID;

public interface MessageService {


    //서비스 로직
    UUID create(MessageCreateDTO messageCreateDTO);
    Message find(UUID id);
    List<Message> findAll();
    List<Message> findAllByChannelId(UUID ChannelId);
    Message update(UUID id, MessageUpdateDTO messageUpdateDTO);
    UUID delete(UUID id);
}