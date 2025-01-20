package com.sprint.mission.discodeit.repository.file.channel;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.repository.jcf.channel.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileAbstractRepository;
import java.util.UUID;

public class FileChannelRepository extends FileAbstractRepository<Channel, UUID> implements ChannelRepository {
    private static final String CHANNEL_FILE_PATH_NAME = "temp/file/user/channel.ser";
    private static ChannelRepository FILE_USER_REPOSITORY_INSTANCE;

    protected FileChannelRepository() {
        super(CHANNEL_FILE_PATH_NAME);
        store.putAll(loadFile());
    }

    public ChannelRepository getInstance() {
        if (FILE_USER_REPOSITORY_INSTANCE == null) {
            FILE_USER_REPOSITORY_INSTANCE = new FileChannelRepository();
        }
        return FILE_USER_REPOSITORY_INSTANCE;
    }
}
