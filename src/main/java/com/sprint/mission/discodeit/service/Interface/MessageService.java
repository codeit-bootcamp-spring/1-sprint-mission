package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    //생성
    Message createMessage(CreateMessageRequest request);

    //읽기
    Optional<Message> getMessageById(UUID id);

    //모두 읽기
    List<Message> getAllMessages();
    List<Message> findAllByChannelId(UUID channelID);

    //수정
    Message updateMessage(UpdateMessageRequest request);

    //삭제
    void deleteMessage(UUID id);
    void deleteByChannelId(UUID channelID);
}
