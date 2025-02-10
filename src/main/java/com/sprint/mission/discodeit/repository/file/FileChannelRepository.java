package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.logger.repository.RepositoryLogger;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ExitStatus.DIR_CREATION_ERROR;


public class FileChannelRepository implements ChannelRepository {
    private static final RepositoryLogger logger = RepositoryLogger.getInstance();
    private static final Path   FILE_DIR  = Paths.get(System.getProperty("user.dir"), "file", "channel");
    private static final String FILE_EXT  = ".ser";

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
     * Create the Channel while ignoring the {@code createAt} and {@code updateAt} fields from {@code channelInfoToCreate}
     */
    @Override
    public Channel createChannel(Channel channelInfoToCreate) {
        Path filePath = getFilePath(channelInfoToCreate.getId());
        if (Files.exists(filePath)) {
            return Channel.createEmptyChannel();
        }

        try (
                FileOutputStream   fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            Channel channelToCreate = Channel.createChannel(
                    channelInfoToCreate.getId(),
                    channelInfoToCreate.getName()
            );

            oos.writeObject(channelToCreate);
            return channelToCreate;
        } catch (IOException e) {
            logger.severe(e);
            
        }

        return Channel.createEmptyChannel();
    }

    @Override
    public Channel findChannelById(UUID key) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return Channel.createEmptyChannel();
        }

        try (
                FileInputStream   fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.severe(e);
            
        }

        return Channel.createEmptyChannel();
    }

    /**
     * Update the Channel while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from {@code channelInfoToUpdate}
     */
    @Override
    public Channel updateChannelById(UUID key, Channel channelInfoToUpdate) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return Channel.createEmptyChannel();
        }

        Channel existingChannel = findChannelById(key);
        try (
                FileOutputStream   fis = new FileOutputStream(filePath.toFile());
                ObjectOutputStream ois = new ObjectOutputStream(fis);
        ) {
            Channel channelToUpdate = Channel.createChannel(
                    key,
                    existingChannel.getCreateAt(),
                    channelInfoToUpdate.getName()
            );

            ois.writeObject(channelToUpdate);
            return channelToUpdate;
        } catch (IOException e) {
            logger.severe(e);
            
        }

        return Channel.createEmptyChannel();
    }

    @Override
    public Channel deleteChannelById(UUID key) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return Channel.createEmptyChannel();
        }

        Channel exsitingChannel = findChannelById(key);
        try {
            Files.delete(filePath);
            return exsitingChannel;
        } catch (IOException e) {
            logger.severe(e);
            
        }

        return Channel.createEmptyChannel();
    }

    private Path getFilePath(UUID key) {
        return FILE_DIR.resolve(key.toString().concat(FILE_EXT));
    }
}
