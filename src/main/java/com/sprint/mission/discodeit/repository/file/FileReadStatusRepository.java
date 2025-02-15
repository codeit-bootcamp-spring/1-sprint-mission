package com.sprint.mission.discodeit.repository.file;


import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    @Override
    public void delete(UUID id) {

    }
}
