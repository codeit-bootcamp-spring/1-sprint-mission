package com.sprint.misson.discordeit.service;

import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.Message;
import com.sprint.misson.discordeit.entity.User;

import java.util.List;

public interface MessageService {


    //생성
    Message createMessage(User user, String content, Channel channel);

    //모두 읽기
    List<Message> getMessages();

    //읽기
    Message getMessageByUUID(String messageId);

    //다건 조회 - 내용
    List<Message> getMessageByContent(String content);

    //다건 조회 - 작성자
    List<Message> getMessageBySender(User sender);

    //다건 조회 - 날짜
    List<Message> getMessageByCreatedAt(Long createdAt);

    //다건 조회 - 특정 채널
    List<Message> getMessagesByChannel(Channel channel);

    //수정
    Message updateMessage(String messageId, String content);

    //삭제
    boolean deleteMessage(Message message);
}
