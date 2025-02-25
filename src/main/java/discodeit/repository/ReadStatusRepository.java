package discodeit.repository;

import discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findAll();
    List<ReadStatus> findAllByUserId(UUID userId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
    void deleteByChannelId(UUID channelId);
}
