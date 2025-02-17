package some_path._1sprintmission.discodeit.repository.jcf;


import some_path._1sprintmission.discodeit.dto.MessageUpdateRequestDTO;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        this.data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public Message update(MessageUpdateRequestDTO request) {
        return null;
    }

    @Override
    public List<Message> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.data.remove(id);
    }

    @Override
    public void deleteBySender(UUID senderId) {
        this.data.entrySet().removeIf(entry -> entry.getValue().getSender().equals(senderId));
    }

    @Override
    public void deleteByChannelId(UUID channelId) {

    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return null;
    }
}