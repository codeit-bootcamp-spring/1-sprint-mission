package discodeit.service.file;

import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {

    private final Path directory;
    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.directory = Paths.get("src", "main", "resources", "data", "serialized", "messages");
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void updateUserService(UserService userService) {

    }

    @Override
    public void updateChannelService(ChannelService channelService) {

    }

    @Override
    public Message create(String content, User sender, UUID channelId) {
        return null;
    }

    @Override
    public Message find(UUID messageId) {
        return null;
    }

    @Override
    public List<Message> findAll() {
        return List.of();
    }

    @Override
    public String getInfo(Message message) {
        return "";
    }

    @Override
    public void update(UUID messageId, String content) {

    }

    @Override
    public void delete(UUID messageId) {

    }
}
