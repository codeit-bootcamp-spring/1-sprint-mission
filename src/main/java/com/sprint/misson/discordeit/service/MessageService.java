package com.sprint.misson.discordeit.service;

import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.Message;
import com.sprint.misson.discordeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {


    //생성
    public boolean createMessage( User user, String content, Channel channel);

    //모두 읽기
    public List<Message> getMessages();

    //읽기
    public Message getMessageByUUID(String messageId);

    //다건 조회 - 내용
    public List<Message> getMessageByContent(String content);

    //다건 조회 - 작성자
    public List<Message> getMessageBySender(User sender);

    //다건 조회 - 날짜
    public List<Message> getMessageByCreatedAt(Long createdAt);

    //다건 조회 - 특정 채널
    public List<Message> getMessagesByChannel(Channel channel);

    //수정
    public boolean updateMessage(Message message);

    //삭제
    public boolean deleteMessage(Message message);
}
