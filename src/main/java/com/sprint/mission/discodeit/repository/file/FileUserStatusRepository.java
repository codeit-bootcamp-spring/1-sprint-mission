package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.userstatus.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserStatusRepository extends AbstractFileRepository implements UserStatusRepository {
    public FileUserStatusRepository(@Value("${data.path.userstatus}") String FILE_PATH) {
        super(FILE_PATH);
    }


}
