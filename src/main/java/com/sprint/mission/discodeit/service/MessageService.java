package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.message.*;

import com.sprint.mission.discodeit.entity.message.create.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.message.create.MessageCreateResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  /**
   * 메시지를 생성하고, 선택적으로 첨부파일을 추가합니다.
   */
  MessageCreateResponse create(MessageCreateRequest createRequest,
      MessageSendFileRequest fileRequest) throws IOException;

  /**
   * ID로 메시지를 조회합니다. 첨부파일 정보도 포함됩니다.
   */
  MessageResponse getMessageById(UUID messageId);

  /**
   * 특정 채널의 모든 메시지를 조회합니다.
   */
  Map<UUID, MessageResponse> findAllMessageByChannelId(String channelId);

  /**
   * 메시지를 업데이트합니다.
   */
  MessageResponse updateMessage(String messageId, MessageUpdateRequest updateRequest);

  /**
   * 메시지와 관련된 첨부파일을 모두 삭제합니다.
   */
  UUID deleteMessage(String messageId);

  MessageAndFileCreateResponse create(MessageAndFileCreateRequest request,
      List<MultipartFile> file);

  List<MessageAndFileCreateResponse> findAllMessage(UUID channelId);

  void remove(UUID messageId);

  MessageUpdateResponse updateMessage(UUID messageId, MessageContentUpdateRequest request);
}
