package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;


import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {


  //서비스 로직
  Message create(MessageCreateDTO dto, List<MultipartFile> files);

  Message find(UUID id);

  List<Message> findAll();

  List<Message> findAllByChannelId(UUID ChannelId);

  Message update(UUID id, MessageUpdateDTO messageUpdateDTO);

  UUID delete(UUID id);
}