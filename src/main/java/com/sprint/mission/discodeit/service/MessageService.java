package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

/*
 *  CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 구현한 인터페이스
 * */

public interface MessageService {
    void addMessage(Message message);
    Message getMessage(UUID id);
    List<Message> getAllMessages();
    void updateMessage(UUID id, String newContent);
    void deleteMessage(UUID id);
}
