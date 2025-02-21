package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.discodeit.constant.ExitStatus.DIR_CREATION_ERROR;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.logger.repository.RepositoryLogger;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileMessageRepository implements MessageRepository {

    private static final RepositoryLogger logger = RepositoryLogger.getInstance();
    private static final Path FILE_DIR = Paths.get(System.getProperty("user.dir"), "file",
        "message");
    private static final String FILE_EXT = ".ser";

    static {
        if (!Files.exists(FILE_DIR)) {
            try {
                Files.createDirectories(FILE_DIR);
            } catch (IOException e) {
                System.err.println("Failed to mkdir: " + e.getMessage());
                System.exit(DIR_CREATION_ERROR.ordinal());
            }
        }
    }

    /**
     * Create the Message while ignoring the {@code createAt} and {@code updateAt} fields from
     * {@code messageInfoToCreate}
     */
    @Override
    public Message createMessage(Message messageInfoToCreate) {
        Path filePath = getFilePath(messageInfoToCreate.getId());
        if (Files.exists(filePath)) {
            return Message.EMPTY_MESSAGE;
        }

        try (
            FileOutputStream fos = new FileOutputStream(filePath.toFile());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            Message messageToCreate = Message.createMessage(
                messageInfoToCreate.getId(),
                messageInfoToCreate.getContent()
            );

            oos.writeObject(messageToCreate);
            return messageToCreate;
        } catch (IOException e) {
            logger.severe(e);

        }

        return Message.EMPTY_MESSAGE;
    }

    @Override
    public Message findMessageById(UUID key) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return Message.EMPTY_MESSAGE;
        }

        try (
            FileInputStream fis = new FileInputStream(filePath.toFile());
            ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.severe(e);

        }

        return Message.EMPTY_MESSAGE;
    }

    /**
     * Update the Message while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields
     * from {@code messageInfoToUpdate}
     */
    @Override
    public Message updateMessageById(UUID key, Message messageInfoToUpdate) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return Message.EMPTY_MESSAGE;
        }

        Message existingMessage = findMessageById(key);
        try (
            FileOutputStream fis = new FileOutputStream(filePath.toFile());
            ObjectOutputStream ois = new ObjectOutputStream(fis);
        ) {
            Message messageToUpdate = Message.createMessage(
                key,
                existingMessage.getCreateAt(),
                messageInfoToUpdate.getContent()
            );

            ois.writeObject(messageToUpdate);
            return messageToUpdate;
        } catch (IOException e) {
            logger.severe(e);

        }

        return Message.EMPTY_MESSAGE;
    }

    @Override
    public Message deleteMessageById(UUID key) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return Message.EMPTY_MESSAGE;
        }

        Message exsitingMessage = findMessageById(key);
        try {
            Files.delete(filePath);
            return exsitingMessage;
        } catch (IOException e) {
            logger.severe(e);

        }

        return Message.EMPTY_MESSAGE;
    }

    private Path getFilePath(UUID key) {
        return FILE_DIR.resolve(key.toString().concat(FILE_EXT));
    }
}
