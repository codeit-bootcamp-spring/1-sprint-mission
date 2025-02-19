package some_path._1sprintmission.discodeit.repository;

import some_path._1sprintmission.discodeit.entiry.ReadStatus;
import some_path._1sprintmission.discodeit.entiry.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    void deleteByUser(User user);

    ReadStatus save(ReadStatus readStatus);

    void deleteByChannelId(UUID channelId);
    Optional<ReadStatus> findById(UUID id);

    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(ReadStatus readStatus);
    void deleteById(UUID id);
    Optional<ReadStatus> findByUserAndChannel(UUID user, UUID channel);
}
