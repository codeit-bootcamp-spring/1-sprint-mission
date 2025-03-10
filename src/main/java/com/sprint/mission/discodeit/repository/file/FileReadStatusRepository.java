package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private final Path DIRECTORY;

    public FileReadStatusRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "ReadStatus.ser");
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createFile(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        List<ReadStatus> readStatuses = new ArrayList<>();
        try {
            if (Files.exists(DIRECTORY) && Files.size(DIRECTORY) > 0) { // 파일이 존재하고 내용이 있는 경우에만 읽기
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORY.toFile()))) {
                    while (true) {
                        try {
                            ReadStatus existingReadStatus = (ReadStatus) ois.readObject();
                            readStatuses.add(existingReadStatus);
                        } catch (EOFException e) {
                            break;
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            // Files.size()에서 발생하는 IOException 처리
            if (e instanceof NoSuchFileException) {
                // 파일이 존재하지 않는 경우, 빈 리스트 유지
            } else {
                throw new RuntimeException(e);
            }
        }

        // 읽음 상태 업데이트 또는 추가
        boolean readStatusUpdated = false;
        for (int i = 0; i < readStatuses.size(); i++) {
            if (readStatuses.get(i).getUserId().equals(readStatus.getUserId()) &&
                    readStatuses.get(i).getChannelId().equals(readStatus.getChannelId())) {
                readStatuses.set(i, readStatus);
                readStatusUpdated = true;
                break;
            }
        }
        if (!readStatusUpdated) {
            readStatuses.add(readStatus);
        }

        // 파일에 다시 쓰기
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORY.toFile()))) {
            for (ReadStatus rs : readStatuses) {
                oos.writeObject(rs);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return readStatus;
    }
    @Override
    public Optional<ReadStatus> findById(UUID id) {
        List<ReadStatus> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        List<ReadStatus> result = new ArrayList<>();
        if (Files.exists(DIRECTORY)) {
            try (
                    FileInputStream fis = new FileInputStream(DIRECTORY.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                while (true) {
                    try {
                        ReadStatus readStatus = (ReadStatus) ois.readObject();
                        if (readStatus.getUserId().equals(userId)) {
                            result.add(readStatus);
                        }
                    } catch (java.io.EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error reading ReadStatus from file: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        return result;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        List<ReadStatus> result = new ArrayList<>();
        if (Files.exists(DIRECTORY)) {
            try (
                    FileInputStream fis = new FileInputStream(DIRECTORY.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                while (true) {
                    try {
                        ReadStatus readStatus = (ReadStatus) ois.readObject();
                        if (readStatus.getChannelId().equals(channelId)) {
                            result.add(readStatus);
                        }
                    } catch (java.io.EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error reading ReadStatus from file: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        return result;
    }

    @Override
    public List<ReadStatus> findAllByIdIn(List<UUID> ids) {
        List<ReadStatus> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }

    private List<ReadStatus> readAllContents() {
        List<ReadStatus> contents = new ArrayList<>();
        if (Files.exists(DIRECTORY)) {
            try (
                    FileInputStream fis = new FileInputStream(DIRECTORY.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                while (true) {
                    try {
                        ReadStatus readStatus = (ReadStatus) ois.readObject();
                        contents.add(readStatus);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return contents;
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(UUID id) {
        List<ReadStatus> allStatuses = readAllContents();
        List<ReadStatus> updatedStatuses = allStatuses.stream()
                .filter(status -> !status.getId().equals(id))
                .toList();
        saveAllContents(updatedStatuses);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        this.findAllByChannelId(channelId)
                .forEach(readStatus -> this.deleteById(readStatus.getId()));
    }

    private void saveAllContents(List<ReadStatus> statuses) {
        try (
                FileOutputStream fos = new FileOutputStream(DIRECTORY.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            for (ReadStatus status : statuses) {
                oos.writeObject(status);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}