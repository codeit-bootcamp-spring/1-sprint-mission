package com.sprint.mission.discodeit.repository.file.message;

import com.sprint.mission.discodeit.entity.message.DirectMessage;
import com.sprint.mission.discodeit.repository.file.FileAbstractRepository;
import com.sprint.mission.discodeit.repository.jcf.message.directMessage.DirectMessageRepository;
import java.util.UUID;

public class FileDirectMessageRepository extends FileAbstractRepository<DirectMessage, UUID> implements
        DirectMessageRepository {
    private static final String DIRECT_MESSAGE_FILE_PATH_NAME = "temp/file/user/channel.ser";
    private static FileDirectMessageRepository FILE_DIRECT_REPOSITORY_INSTANCE;

    protected FileDirectMessageRepository() {
        super(DIRECT_MESSAGE_FILE_PATH_NAME);
        store.putAll(loadFile());
    }

    public static DirectMessageRepository getInstance() {
        if (FILE_DIRECT_REPOSITORY_INSTANCE == null) {
            FILE_DIRECT_REPOSITORY_INSTANCE = new FileDirectMessageRepository();
        }
        return FILE_DIRECT_REPOSITORY_INSTANCE;
    }
}
