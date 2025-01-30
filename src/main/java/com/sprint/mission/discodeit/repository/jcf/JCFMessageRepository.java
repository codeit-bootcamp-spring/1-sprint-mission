package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository {
    private final List<Message> messages;

    public JCFMessageRepository(){
        this.messages = new ArrayList<>();
    }

    public void save(Message message){
        messages.add(message);
    }

    public void delete(Message message){
        messages.remove(message);
    }

    public List<Message> load(){
        return messages;
    }

    public Optional<Message> findById(UUID messageId) {
        return messages.stream()
                .filter(user-> user.getId().equals(messageId))
                .findFirst();
    }

}
