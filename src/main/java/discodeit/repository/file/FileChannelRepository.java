package discodeit.repository.file;

import discodeit.entity.Channel;
import discodeit.entity.User;
import discodeit.repository.ChannelRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {

    private final Path directory;

    public FileChannelRepository() {
        this.directory = Paths.get("src", "main", "resources", "data", "serialized", "channels");
    }

    @Override
    public Channel save(String name, String introduction, User owner) {
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
    public void update(UUID channelId, String name, String introduction) {

    }

    @Override
    public void delete(UUID channelId) {

    }
}
