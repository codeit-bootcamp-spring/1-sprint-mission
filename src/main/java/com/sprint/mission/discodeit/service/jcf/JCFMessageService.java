package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final List<Message> data;

    public JCFMessageService() {
        this.data = new ArrayList<>();
    }


    @Override
    public Message createMessage(UUID id, String userName, String messageContent){
        Message message = new Message(id, userName, messageContent);
        data.add(message);
        return message;
    }


    @Override
    public Message getMessageId(UUID id){
        for(Message message : data){
            if(message.getId().equals(id)){
                return message;
            }
        }
        return null;
    }


    @Override
    public List<Message> getAllMessages(){
        return new ArrayList<>(data);
    }


    @Override
    public void updateMessage(UUID id, String newMessage){
        Message message = getMessageId(id);
        if(message != null){
            message.setMessage(newMessage);
        }
    }


    @Override
    public void deleteMessage(UUID id){
        Message message = getMessageId(id);
        if (message != null){
            data.remove(message);
        }
    }

}












