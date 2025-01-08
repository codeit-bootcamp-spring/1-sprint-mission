package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCF_message implements MessageService {

    private final List<Message> messageSet;

    public JCF_message() {
        messageSet = new ArrayList<>();
    }

    @Override
    public void Creat(Message message) {
        messageSet.add(message);

    }

    @Override
    public void Delete(Message message) {
        messageSet.remove(message);
    }

    @Override
    public void Update(Message message, Message updateMessage) {
        messageSet.replaceAll(messages -> messages.equals(message) ? updateMessage : messages);
    }

    @Override
    public List<Message> Write(UUID id) {
        return messageSet.stream().filter(message_id -> message_id.GetId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public List<Message> AllWrite() {
        return messageSet;
    }
}
