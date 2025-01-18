package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JcfMessageService implements MessageService {
    private final Map<UUID, Message> data=new HashMap<>();

    @Override
    public void messageSave(Message message) {
        data.put(message.getId(),message);
    }

    @Override
    public Optional<Message> messageList(UUID id) {
        return Optional.ofNullable(data.get(id));
       /* for (Message message : data) {
            if(message.getId().equals(id)){
                return Optional.of(message);
            }
        }
        return Optional.empty();*/
    }

    @Override
    public List<Message> messageAllList() {
        List<Message> messageList=new ArrayList<>();
        for (UUID uuid : data.keySet()) {
            messageList.add(data.get(uuid));
        }
        return messageList;
    }

    @Override
    public void updateMessage(Message updateMessage) {
        if(data.containsKey(updateMessage.getId())) {
            data.put(updateMessage.getId(),updateMessage);
        }
       /* for (Message message : data)
            if (message.getId().equals(updateMessage.getId())) {
                message.updateContent(updateMessage.getContent());
            }*/
    }

    @Override
    public void deleteMessage(UUID id) {
        if(data.containsKey(id)) {
            data.remove(id);
        }
       /* for (Message message : data)
            if (message.getId().equals(id)) {
                data.remove(message);
            }*/
    }
}
