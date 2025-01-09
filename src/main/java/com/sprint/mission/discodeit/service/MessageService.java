package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface MessageService {

    // 메세지 생성
    Message createMessage(Channel channel, User writer, String content);

    // 채널 메세지 모두 조회
    List<Message> getAllMessageList();

    // 채널명으로 메세지 조회
    List<Message> searchByChannel(Channel channel);

    // 메세지 삭제
    void deleteMessage(Message message);

    // 메세지 정보 출력
    void displayInfo(Message message);

    // 메세지 수정
    void updateMessage(Message message, String content);
}
