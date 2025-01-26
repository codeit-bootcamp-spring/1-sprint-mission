package discodeit.service.file;

import discodeit.entity.Channel;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.validator.ChannelValidator;
import discodeit.validator.UserValidator;

import java.io.*;
import java.nio.file.Files;
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
        validator.validate(name, introduction);
        Channel channel = new Channel(name, introduction, owner);
        Path filePath = directory.resolve(channel.getId() + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Path filePath = directory.resolve(channelId + ".ser");
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            Object data = ois.readObject();
            return (Channel) data;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Channel> findAll() {
        try {
            List<Channel> channels = Files.list(directory)
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ) {
                            Object data = ois.readObject();
                            return (Channel) data;
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            return channels;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
