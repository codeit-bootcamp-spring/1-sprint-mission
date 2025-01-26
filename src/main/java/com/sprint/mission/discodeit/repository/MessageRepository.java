package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    //메시지 생성
    Message createMessage(UUID channelId, UUID userId, String content);

    //메세지 조회 -ALL
    List<Message> getAllMessageList();


    //MessageID로 메세지 조회
    Message searchById(UUID messageId);

    //Message 수정
    void updateMessage(UUID messageId, String content);

    //Message 삭제
    void deleteMessage(UUID messageId);
}


