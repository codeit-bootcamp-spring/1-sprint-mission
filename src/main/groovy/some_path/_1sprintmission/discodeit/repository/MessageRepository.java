package some_path._1sprintmission.discodeit.repository;

import some_path._1sprintmission.discodeit.dto.MessageUpdateRequestDTO;
import some_path._1sprintmission.discodeit.entiry.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findById(UUID id);
    Message update(MessageUpdateRequestDTO request);
    List<Message> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID messageId);

    void deleteBySender(UUID senderId);

    void deleteByChannelId(UUID channelId);
    List<Message> findAllByChannelId(UUID channelId);
}
