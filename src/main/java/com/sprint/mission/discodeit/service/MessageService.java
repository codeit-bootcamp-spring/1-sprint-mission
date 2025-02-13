package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.Collection;
import java.util.UUID;

public interface MessageService {
    UUID createMessage(MessageCreateRequest messageCreateRequest);

    // Read : 전체 메세지 조회, 특정 메세지 읽기
    Collection<Message> findAllByChannelId(UUID channelId);

    Message getMessageById(UUID id);

    // Update : 특정 메세지 수정
    void updateMessageText(MessageUpdateRequest messageUpdateRequest);

    // Delete : 특정 메세지 삭제
    void deleteMessageById(UUID id);
}
