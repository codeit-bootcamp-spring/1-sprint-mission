package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    // 메세지 생성
    Message createMessage(Channel channel, User writer, String content);

    // 메세지 모두 조회
    List<Message> getAllMessageList();

    // id로 메세지 조회
    Message searchById(UUID messageId);

    // 메세지 정보 출력
    void printMessageInfo(Message message);
    void printMessageListInfo(List<Message> messageList);
    
    // 메세지 삭제
    void deleteMessage(Message message);

    // 메세지 수정
    void updateMessage(Message message, String content);
}
