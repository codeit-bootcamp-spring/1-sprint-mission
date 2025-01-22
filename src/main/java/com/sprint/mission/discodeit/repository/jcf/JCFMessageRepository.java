package com.sprint.mission.discodeit.repository.jcf;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    Map<UUID, Message> messages;
    public JCFMessageRepository(){
        this.messages = new HashMap<>();
    }
    public void saveMessage(Message message){
        messages.put(message.getId(), message);
    }
    public Optional<Message> findMessageById(UUID id){
        return Optional.ofNullable(messages.get(id));
    }
    public Map<UUID, Message> findAllMessages() {
        return messages;
    }
    public void deleteMessageById(UUID id){
        messages.remove(id);
    }
    public void deleteAllMessages(){ messages.clear(); };
}
