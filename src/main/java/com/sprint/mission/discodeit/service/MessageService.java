package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

  // 추가된 메서드: multipart/form-data를 처리하기 위한 메서드
  Message create(UUID authorId, UUID channelId, String content,
      List<BinaryContentCreateRequest> attachments);

  Message getMessageById(UUID messageId);

  List<Message> getMessagesByChannel(UUID channelId);

  List<Message> getMessagesByAuthor(UUID authorId);  // getMessagesBySender에서 변경

  Message updateMessageContent(UUID messageId, String newContent);

  boolean deleteMessage(UUID messageId);
}