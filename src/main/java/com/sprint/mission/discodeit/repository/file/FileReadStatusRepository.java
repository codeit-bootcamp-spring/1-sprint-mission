package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "file")
public class FileReadStatusRepository  implements ReadStatusRepository {
    @Override
    public void save(ReadStatus readStatus) {

    }

    @Override
    public ReadStatus findById(UUID id) {
        return null;
    }

    @Override
    public List<ReadStatus> findByUserIdAndChannelId(UUID userid, UUID channelId) {
        return null;
    }
}
