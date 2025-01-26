package discodeit.repository.file;

import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.repository.MessageRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {

    private final Path directory;

    public FileMessageRepository(Path directory) {
        this.directory = Paths.get("src", "main", "resources", "data", "serialized", "messages");
    }

    @Override
    public Message save(String content, User sender, UUID channelId) {
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
    public void update(UUID messageId, String content) {

    }

    @Override
    public void delete(UUID messageId) {

    }
}
