package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;


import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {


  Message create(MessageCreateDTO dto,
      List<BinaryContentCreateRequest> binaryContentCreateRequests);

  Message find(UUID id);

  List<Message> findAll();

  List<Message> findAllByChannelId(UUID ChannelId);

  Message update(UUID id, MessageUpdateDTO messageUpdateDTO);

  void delete(UUID id);
}