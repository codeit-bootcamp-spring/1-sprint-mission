package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageResponse createMessage(MessageRequest.Create request, List<MultipartFile> messageFiles);

  PageResponse<MessageResponse> findAllByChannelId(UUID channelId);

  MessageResponse findById(UUID id);

  MessageResponse update(UUID id, MessageRequest.Update request);

  void deleteById(UUID id);

}
