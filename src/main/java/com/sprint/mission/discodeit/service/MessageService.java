package com.sprint.mission.discodeit.service;


import java.util.List;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;


public interface MessageService {
    // 생성
    Message createMessage(String content, Channel channel, User sender);

    // 내용 수정
    Message updateContent(Message message, String content);

    // 조회
    Message findMessageById(Message m);
    List<Message> findMessageByChannel(Message message);
    List<Message> findAllMessage();

    // 프린트
    void printMessage(Message message);
    void printMessagesList(List<Message> messages);

    // 삭제
    void deleteMessage(Message message);
}
