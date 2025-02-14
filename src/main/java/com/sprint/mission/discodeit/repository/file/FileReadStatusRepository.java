package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Primary
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private final String directory = "read_statuses";
    private final String FILE_EXTENSION = ".ser";

    private final FileManager fileManager = new FileManager(directory);
    private final Path filePath = fileManager.getPath();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Path path = filePath.resolve(readStatus.getId().toString().concat(FILE_EXTENSION));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(readStatus);
            return readStatus;
        } catch (IOException e) {
            throw new FileIOException("ReadStatus 저장 실패");
        }
    }

    @Override
    public ReadStatus findById(UUID id) {
        Path path = filePath.resolve(id.toString().concat(FILE_EXTENSION));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (ReadStatus) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 ReadStatus입니다.");
        }
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        File[] files = filePath.toFile().listFiles();
        List<ReadStatus> readStatuses = new ArrayList<>(100);

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
        Path path = filePath.resolve(id.toString().concat(FILE_EXTENSION));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("ReadStatus 삭제 실패");
            }
        }
    }
}
