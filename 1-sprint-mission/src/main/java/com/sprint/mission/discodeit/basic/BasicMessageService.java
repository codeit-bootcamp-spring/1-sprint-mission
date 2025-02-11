package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.DTO.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFBinaryContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("BasicMessageService")
public class BasicMessageService implements MessageService {
    private final MessageService messageService;




    @Autowired
    public BasicMessageService(@Qualifier("FileMessageService") MessageService messageService){
        this.messageService= messageService;
    }

    @Override
    public void createNewMessage(String title, String body) {
        messageService.createNewMessage(title,body);
    }

    @Override
    public void createNewMessage(String title, String body, List<BinaryContent> binaryContents) {
        messageService.createNewMessage(title,body,binaryContents);
    }

    @Override
    public void createNewMessagetoImg(String title, String body, List<char[]> imgs) {


        messageService.createNewMessagetoImg(title,body,imgs);
    }

    @Override
    public <T> List<Message> readMessage(T key) {
        return messageService.readMessage(key);
    }

    @Override
    public List<Message> readMessageAll() {
        return messageService.readMessageAll();
    }

    @Override
    public boolean updateMessageTitle(UUID ID, String change) {
        return messageService.updateMessageTitle(ID,change);
    }

    @Override
    public boolean updateMessageTitle(String name, String change) {
        return messageService.updateMessageTitle(name,change);
    }

    @Override
    public boolean updateMessageBody(UUID ID, String change) {
        return messageService.updateMessageBody(ID,change);
    }

    @Override
    public boolean updateMessageBody(String name, String change) {
        return messageService.updateMessageBody(name,change);
    }

    @Override
    public boolean deleteMessage(UUID id) {
        return messageService.deleteMessage(id);
    }

    @Override
    public boolean deleteMessage(String title) {
        return messageService.deleteMessage(title);
    }

    @Override
    public void addMessage(Message m) {
        messageService.addMessage(m);
    }

    @Override
    public boolean updateMessageTitle(MessageDto messageDto) {
        return messageService.updateMessageTitle(messageDto);
    }

    @Override
    public boolean updateMessageBody(MessageDto  messageDto) {
        return messageService.updateMessageBody(messageDto);
    }
}
