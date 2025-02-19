package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    @Override
    public void delete(UUID id) {

    }
}
