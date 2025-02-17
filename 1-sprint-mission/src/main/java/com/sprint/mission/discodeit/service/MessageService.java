package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.message.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createNewMessage(String title, String body);

    void createNewMessage(String title, String body, List<BinaryContent> binaryContents);


    void createNewMessagetoImg(String title, String body, List<char[]> imgs);

    <T> List<Message> readMessage(T key);


    List<Message> readMessageAll();

    boolean updateMessageTitle(UUID ID, String change);

    boolean updateMessageTitle(String name, String change);

    boolean updateMessageBody(UUID ID, String change);

    boolean updateMessageBody(String name, String change);

    boolean deleteMessage(UUID id);

    boolean deleteMessage(String title);
    void addMessage(Message m);
    boolean updateMessageTitle(MessageDto messageDto);
    public boolean updateMessageBody(MessageDto messageDto);
    /// /////////////////////////////////////////////////////
}
