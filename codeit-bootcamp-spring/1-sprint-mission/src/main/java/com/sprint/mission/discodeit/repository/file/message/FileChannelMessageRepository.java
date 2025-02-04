package com.sprint.mission.discodeit.repository.file.message;

import com.sprint.mission.discodeit.entity.message.ChannelMessage;
import com.sprint.mission.discodeit.repository.file.FileAbstractRepository;
import com.sprint.mission.discodeit.repository.jcf.message.ChannelMessage.ChannelMessageRepository;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileChannelMessageRepository extends FileAbstractRepository<ChannelMessage, UUID>
        implements ChannelMessageRepository {

    private static final String CHANNEL_MESSAGE_FILE_PATH_NAME = "temp/file/user/channelMessage.ser";
    private static ChannelMessageRepository INSTANCE;


    protected FileChannelMessageRepository() {
        super(CHANNEL_MESSAGE_FILE_PATH_NAME);
        store.putAll(loadFile());
    }

    public static ChannelMessageRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileChannelMessageRepository();
        }
        return INSTANCE;
    }

}
