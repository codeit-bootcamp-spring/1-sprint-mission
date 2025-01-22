package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileMessageService;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.UUID;

public class BasicMessageService implements MessageService {
    private MessageService messageService;

    public BasicMessageService(){
        this.messageService= new FileMessageService();
    }
    public void setUpJfcMessage(){
        this.messageService= new JCFMessageService();
    }

    public void setUpFileMessage(){
        this.messageService= new FileMessageService();
    }

    @Override
    public void createNewMessage(String title, String body) {
        messageService.createNewMessage(title,body);
    }

    @Override
    public <T> String readMessage(T key) {
        return messageService.readMessage(key);
    }

    @Override
    public String readMessageAll() {
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
}
