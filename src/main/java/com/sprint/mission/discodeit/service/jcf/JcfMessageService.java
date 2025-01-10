package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JcfMessageService implements MessageService {
    private List<Message> data=new ArrayList<>();

    @Override
    public void messageSave(Message message) {
        data.add(message);
    }

    @Override
    public Optional<Message> messageList(UUID id) {
        for (Message message : data) {
            if(message.getId().equals(id)){
                return Optional.of(message);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Message> messageAllList() {
        return data;
    }

    @Override
    public void updateMessage(Message updateMessage) {
        for (Message message : data)
            if (message.getId().equals(message.getId())) {
                message.updateContent(updateMessage.getContent());
            }
    }

    @Override
    public void deleteMessage(UUID id) {
        for (Message message : data)
            if (message.getId().equals(id)) {
                data.remove(message);
            }
    }
}
