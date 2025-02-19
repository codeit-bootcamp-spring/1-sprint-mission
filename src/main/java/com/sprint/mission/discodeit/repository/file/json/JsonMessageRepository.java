package com.sprint.mission.discodeit.repository.file.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.RepositoryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class JsonMessageRepository extends JsonRepository<UUID, Message> implements MessageRepository {
    public JsonMessageRepository(RepositoryProperties properties) {
        super(
                new ObjectMapper().registerModule(new JavaTimeModule()),
                properties,
                "messages.json",
                new TypeReference<HashMap<UUID, Message>>() {}
        );
    }

    @Override
    public Message saveMessage(Message message) {
        map.put(message.getId(), message);
        saveToJson();
        return message;
    }

    @Override
    public Message findMessageById(UUID messageId) {
        loadFromJson();
        return map.get(messageId);
    }

    @Override
    public Map<UUID, Message> findAllMessage() {
        loadFromJson();
        return map;
    }

    @Override
    public void removeMessageById(UUID messageId) {
        map.remove(messageId);
        saveToJson();
    }
}
