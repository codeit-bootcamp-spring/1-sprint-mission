package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JcfMessageService implements MessageService {
    private final List<Message> data= new ArrayList<>();

    @Override
    public void create(Message message) {
        data.add(message);
    }

    @Override
    public void update(UUID id, Message updateMessage) {
        for (Message message : data) {
            if (message.getId().equals(id)) {
                message.updateContent(updateMessage.getContent());
                return;
            }
        }
    }

    @Override
    public void delete(UUID id) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(id)) {
                data.remove(i);
                return;
            }
        }
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data);
    }
}
