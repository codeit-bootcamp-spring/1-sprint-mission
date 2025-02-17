package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class JcfMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data=new HashMap<>();
    @Override
    public void createMessage(UUID id, Message message) {
        data.put(id, message);
    }

    @Override
    public void updateMessage(UUID id, String content) {
        data.get(id).updateMessage(content);
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

}
