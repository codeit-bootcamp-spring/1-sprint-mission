package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageResponse;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.enity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // Create
    Message create(MessageCreateRequest messageCreateRequest); // 메세지 전송

    // Read
    MessageResponse find(UUID messageId);  // 메세지 단건 조회

    List<MessageResponse> findAllByChannelId(UUID channelId, UUID userId); // 모든 메세지 조회

    // Update
    void update(UUID messageId, MessageUpdateRequest messageUpdateRequest);    // 메시지 수정

    // Delete
    void delete(UUID messageId);    // 메세지 삭제

}
