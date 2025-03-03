package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserStatusRepository implements UserStatusRepository {

    private final Path DIRECTORY;

    public FileUserStatusRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "UserStatus.ser");
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createFile(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        try (
                FileOutputStream fos = new FileOutputStream(DIRECTORY.toFile(), true);
                ObjectOutputStream oos = new ObjectOutputStream(fos) {
                    @Override
                    protected void writeStreamHeader() throws IOException {
                        if (fos.getChannel().position() == 0) {
                            super.writeStreamHeader();
                        } else {
                            reset();
                        } //역직렬화 헤더 오류 해결 코드. 파일에 한번만 헤더 들어갈 수 있도록
                    }
                }
        ) {
            oos.writeObject(userStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        List<UserStatus> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAllByIdIn(List<UUID> ids) {
        List<UserStatus> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return this.readAllContents().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

//    @Override
//    public List<UserStatus> readAllUserStatus() {
//        List<UserStatus> contents = new ArrayList<>();
//        if (Files.exists(DIRECTORY)) {
//            try (
//                    FileInputStream fis = new FileInputStream(DIRECTORY.toFile());
//                    ObjectInputStream ois = new ObjectInputStream(fis)
//            ) {
//                while (true) {
//                    try {
//                        UserStatus content = (UserStatus) ois.readObject();
//                        contents.add(content);
//                    } catch (EOFException e) {
//                        break;
//                    }
//                }
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//        return contents;
//    }
    @Override
    public List<UserStatus> readAllContents() {
        List<UserStatus> contents = new ArrayList<>();
        if (Files.exists(DIRECTORY)) {
            try (
                    FileInputStream fis = new FileInputStream(DIRECTORY.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                while (true) {
                    try {
                        UserStatus userStatus = (UserStatus) ois.readObject();
                        contents.add(userStatus);
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
        List<UserStatus> allStatuses = readAllContents();
        List<UserStatus> updatedStatuses = allStatuses.stream()
                .filter(status -> !status.getId().equals(id))
                .toList();
        saveAllContents(updatedStatuses);
    }

    private void saveAllContents(List<UserStatus> statuses) {
        try (
                FileOutputStream fos = new FileOutputStream(DIRECTORY.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            for (UserStatus status : statuses) {
                oos.writeObject(status);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}