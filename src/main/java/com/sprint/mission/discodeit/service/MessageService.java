package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

public interface MessageService {
    Message createMessage(User writer, String content);

    // 메세지 정보 출력
    void displayInfo(Message message);

    // 메세지 내용 수정
    void updateMessage(String content);

}
