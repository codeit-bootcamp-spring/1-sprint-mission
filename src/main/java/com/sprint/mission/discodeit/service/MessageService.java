package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateResponse;
import com.sprint.mission.discodeit.dto.message.MessageFindBResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageCreateResponse createMessage(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests);

    // Read : 전체 메세지 조회, 특정 메세지 읽기
    Collection<MessageFindBResponse> findAllByChannelId(UUID channelId);

    MessageFindBResponse getMessageById(UUID id);

    // Update : 특정 메세지 수정
    void updateMessageText(UUID messageId, MessageUpdateRequest messageUpdateRequest);

    // Delete : 특정 메세지 삭제
    void deleteMessageById(UUID id);
}
