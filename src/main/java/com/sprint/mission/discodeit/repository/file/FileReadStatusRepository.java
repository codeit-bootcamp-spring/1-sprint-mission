package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {

    private final Path directoryPath;
    private final String FILE_EXTENSION = ".ser";

    private final FileManager fileManager;

    public FileReadStatusRepository(@Value("${discodeit.repository.file-directory}") String directory, FileManager fileManager) {
        this.fileManager = fileManager;
        this.directoryPath = Path.of(System.getProperty("user.dir"), directory, "read_statuses");
    }

    @PostConstruct
    private void init() {
        fileManager.createDirectory(directoryPath);
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Path path = directoryPath.resolve(readStatus.getId().toString().concat(FILE_EXTENSION));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(readStatus);
            return readStatus;
        } catch (IOException e) {
            throw new FileIOException("ReadStatus 저장 실패");
        }
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        Path path = directoryPath.resolve(id.toString().concat(FILE_EXTENSION));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            ReadStatus readStatus = (ReadStatus) ois.readObject();
            return Optional.of(readStatus);
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        File[] files = directoryPath.toFile().listFiles();
        List<ReadStatus> readStatuses = new ArrayList<>(100);

        if (files == null) {
            return readStatuses;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                ReadStatus readStatus = (ReadStatus) ois.readObject();
                if (readStatus.getUserId().equals(userId)) {
                    readStatuses.add(readStatus);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new FileIOException("ReadStatus 읽기 실패");
            }
        }

        return readStatuses;
    }

    @Override
    public void delete(UUID id) {
        Path path = directoryPath.resolve(id.toString().concat(FILE_EXTENSION));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("ReadStatus 삭제 실패");
            }
        }
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        File[] files = directoryPath.toFile().listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                ReadStatus readStatus = (ReadStatus) ois.readObject();
                if (readStatus.getChannelId().equals(channelId)) {
                    delete(readStatus.getId());
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new FileIOException("ReadStatus 삭제 실패");
            }
        }
    }
}
