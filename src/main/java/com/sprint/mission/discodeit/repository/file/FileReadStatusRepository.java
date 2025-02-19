package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".dat";

    public FileReadStatusRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", ReadStatus.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    @Override
    public void save(ReadStatus readStatus) {
        Path path = resolvePath(readStatus.getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(readStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UUID> findUserIdsByChannelId(UUID channelId) {
        List<UUID> userIds = new ArrayList<>();
        findByCondition(status -> status.getChannel().getId().equals(channelId))
                .forEach(status -> userIds.add(status.getUser().getId()));
        return userIds;
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        findByCondition(status -> status.getChannel().getId().equals(channelId))
                .forEach(status -> deleteById(status.getId()));
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                return Optional.of((ReadStatus) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return findByCondition(status -> status.getUser().getId().equals(userId));
    }

    @Override
    public boolean existsByUserAndChannel(User user, Channel channel) {
        return findByCondition(status -> status.getUser().equals(user) && status.getChannel().equals(channel)).size() > 0;
    }

    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return Files.exists(resolvePath(id));
    }

    private List<ReadStatus> findByCondition(java.util.function.Predicate<ReadStatus> condition) {
        List<ReadStatus> result = new ArrayList<>();
        try {
            Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .forEach(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            ReadStatus status = (ReadStatus) ois.readObject();
                            if (condition.test(status)) {
                                result.add(status);
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
