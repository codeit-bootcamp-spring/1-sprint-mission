package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.CursorResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {

  MessageResponse createMessage(CreateMessageRequest request);

  List<MessageResponse> getMessages();

  PageResponse<MessageResponse> getPageMessages(int page, int size);

  List<MessageResponse> getMessagesByChannel(UUID ChannelID);

  MessageResponse getMessage(UUID uuid);

  MessageResponse updateMessage(UUID id, UpdateMessageRequest request);

  void deleteMessage(UUID uuid);

  CursorResponse<Message> getCursorPages(Instant cursor, int size);
}
