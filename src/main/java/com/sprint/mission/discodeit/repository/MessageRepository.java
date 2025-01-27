package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    //생성
    void createMessage(Message message);

    //읽기
    Optional<Message> getMessageById(UUID id);

    //모두 읽기
    List<Message> getAllMessage();

    //수정
    void updateMessage(UUID id,Message updatedMessage);

    //삭제
    void deleteMessage(UUID id);

}
