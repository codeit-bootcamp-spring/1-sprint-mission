package com.sprint.mission.service;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;

import java.util.List;

public interface MessageService {

    // 메시지 생성
    Message createMessage(User user, Channel channelName , String message);

    // 메시지 변경
    void updateMessage(User user, Channel channel, String afterMessage);

    // 메시지 정보 조회
    List<Message> getAllMessageList();

    // 메시지 출력
    void printChannelMessage(User user);

    // 메시지 삭제
    void deleteMessage(String message);
}
