package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.UUID;

public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository {

    public FileMessageRepository(String FILE_PATH) {
        super(FILE_PATH);
    }

    @Override
    public boolean update(UUID id, String updatedContent) {
        boolean updated = data.computeIfPresent(id, (key, m) -> {
            m.update(updatedContent);
            return m;
        }) != null;
        if (updated) {
            saveData();
        }
        return updated;
    }
}
