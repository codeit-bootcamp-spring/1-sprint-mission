package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageService {
    //메시지 생성
    void createMessage(Message message);
    //메시지 수정
    void updateMessage(Message message);
    //메시지 삭제
    void deleteMessage(String userName, String password);
    //메시지 목록 확인
    List<Message> getAllMessage();

}
