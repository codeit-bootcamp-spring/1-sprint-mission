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
    public void SetUpJfcMessage(){
        this.messageService= new JCFMessageService();
    }

    public void SetUpFileMessage(){
        this.messageService= new FileMessageService();
    }

    @Override
    public void CreateMessageDefault(String title, String body) {
        messageService.CreateMessageDefault(title,body);
    }

    @Override
    public <T> String ReadMessage(T key) {
        return messageService.ReadMessage(key);
    }

    @Override
    public String ReadMessageAll() {
        return messageService.ReadMessageAll();
    }

    @Override
    public boolean UpdateMessageTitle(UUID ID, String change) {
        return messageService.UpdateMessageTitle(ID,change);
    }

    @Override
    public boolean UpdateMessageTitle(String name, String change) {
        return messageService.UpdateMessageTitle(name,change);
    }

    @Override
    public boolean UpdateMessageBody(UUID ID, String change) {
        return messageService.UpdateMessageTitle(ID,change);
    }

    @Override
    public boolean UpdateMessageBody(String name, String change) {
        return messageService.UpdateMessageTitle(name,change);
    }

    @Override
    public boolean DeleteMessage(UUID id) {
        return messageService.DeleteMessage(id);
    }

    @Override
    public boolean DeleteMessage(String title) {
        return messageService.DeleteMessage(title);
    }

    @Override
    public void AddMessage(Message m) {
        messageService.AddMessage(m);
    }
}
