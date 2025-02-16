package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageRepository {
    // 저장
    boolean saveMessage(Message message);

    // 조회
    Message loadMessage(Message message);
    List<Message> getMessagesByChannel(Message message);
    List<Message> loadAllMessages();

    // 삭제
    boolean deleteMessage(Message message);

}
