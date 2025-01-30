package com.sprint.mission.discodeit.repository;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;

import java.util.List;

public interface MessageRepository {

    Message saveMessage(Message message); // 메시지 저장

    void deleteMessage(Message message); // 메시지 삭제

    List<Message> printChannel(Channel channel); // 특정 채널의 메시지 조회

    List<Message> printByUser(User user); // 특정 유저의 메시지 조회

    List<Message> printAllMessage(); // 모든 메시지 조회
}