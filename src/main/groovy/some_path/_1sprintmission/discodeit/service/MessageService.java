package some_path._1sprintmission.discodeit.service;

import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.entiry.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    void create(Channel channel, User sender,Message message);
    Optional<Message> read(UUID id);
    List<Message> readAll();

    void update(UUID id, Message updatedMessage);
    void delete(UUID id);
}