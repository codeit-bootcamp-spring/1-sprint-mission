package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.UUID;

public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository {

    public FileChannelRepository(String FILE_PATH) {
        super(FILE_PATH);
    }
}
