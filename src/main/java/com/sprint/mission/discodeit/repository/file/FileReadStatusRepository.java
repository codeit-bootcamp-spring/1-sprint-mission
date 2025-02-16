package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileReadStatusRepository extends AbstractFileRepository implements ReadStatusRepository {
    public FileReadStatusRepository(@Value("${data.path.readstatus}") String FILE_PATH) {
        super(FILE_PATH);
    }


    @Override
    public ReadStatus save(ReadStatus readStatus) {
        return null;
    }

    @Override
    public ReadStatus findByUserId(UUID userId) {
        return data.
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return List.of();
    }

    @Override
    public Set<UUID> findUsersByChannelId(UUID channelId) {
        return Set.of();
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return Optional.empty();
    }

    @Override
    public void delete(UUID id) {

    }
}
