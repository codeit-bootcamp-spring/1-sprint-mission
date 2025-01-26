package discodeit.service.file;

import discodeit.entity.Channel;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.validator.ChannelValidator;
import discodeit.validator.UserValidator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private final ChannelValidator validator;
    private final Path directory;

    public FileChannelService() {
        this.validator = new ChannelValidator();
        this.directory = Paths.get("src", "main", "resources", "data", "serialized", "channels");
    }

    @Override
    public Channel create(String name, String introduction, User owner) {
        return null;
    }

    @Override
    public Channel find(UUID channelId) {
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return List.of();
    }

    @Override
    public String getInfo(Channel channel) {
        return "";
    }

    @Override
    public void update(UUID channelId, String name, String introduction) {

    }

    @Override
    public void delete(Channel channel) {

    }
}
