package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  Message create(UUID authorId, UUID channelId, String content,
      List<BinaryContentCreateRequest> attachments);

  Message getMessageById(UUID messageId);

  List<Message> getMessagesByChannel(UUID channelId);

  List<Message> getMessagesByAuthor(UUID authorId);

  Message updateMessageContent(UUID messageId, String newContent);

  boolean deleteMessage(UUID messageId);
}