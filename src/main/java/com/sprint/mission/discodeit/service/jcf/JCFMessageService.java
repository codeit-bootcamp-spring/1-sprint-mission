package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final List<Message> data;

    public JCFMessageService() {
        this.data = new ArrayList<>();
    }

    @Override
    public void createMessage(Message message) {
        data.add(message);
    }

    @Override
    public Optional<Message> getMessageById(UUID id) {
        return data.stream().filter(message -> message.getId().equals(id)).findFirst();
    }

    @Override
    public List<Message> getAllMessage() {
        return new ArrayList<>(data);
    }

    @Override
    public void updateMessage(UUID id, Message updatedMessage) {
        for(int i=0;i<data.size();i++) {
            if(data.get(i).getId().equals(id)) {
                // 기존 객체를 수정
                Message existingMessage = data.get(i);
                existingMessage.update(updatedMessage.getContent(), updatedMessage.getSenderUser());
                return;
            }
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        data.removeIf(message -> message.getId().equals(id));
    }
}