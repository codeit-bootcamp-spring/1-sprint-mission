package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageServiceCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageServiceUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;


import java.util.List;
import java.util.UUID;

public interface MessageService {


    //서비스 로직
    UUID create(MessageServiceCreateDTO messageServiceCreateDTO);
    Message find(UUID id);
    List<Message> findAll();
    List<Message> findAllByChannelId(UUID ChannelId);
    Message update(MessageServiceUpdateDTO messageServiceUpdateDTO);
    UUID delete(UUID id);
}