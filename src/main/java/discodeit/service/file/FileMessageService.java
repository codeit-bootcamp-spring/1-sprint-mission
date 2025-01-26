package discodeit.service.file;

import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
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
        userService.find(sender.getId());
        channelService.find(channelId);

        Message message = new Message(content, sender, channelId);
        Path filePath = directory.resolve(message.getId() + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Path filePath = directory.resolve(messageId + ".ser");
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            Object data = ois.readObject();
            return (Message) data;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> findAll() {
        try {
            List<Message> messages = Files.list(directory)
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ) {
                            Object data = ois.readObject();
                            return (Message) data;
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            return messages;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getInfo(UUID messageId) {
        Message message = find(messageId);
        return message.toString();
    }

    @Override
    public void update(UUID messageId, String content) {
        Message message = find(messageId);
        message.updateContent(content);

        Path filePath = directory.resolve(messageId + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UUID messageId) {
        Path filePath = directory.resolve(messageId + ".ser");

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            System.out.println("삭제에 실패하였습니다.");
        }
    }
}
