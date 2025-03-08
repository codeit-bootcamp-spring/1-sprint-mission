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
        List<UserStatus> userStatuses = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORY.toFile()))) {
            while (true) {
                try {
                    UserStatus existingUserStatus = (UserStatus) ois.readObject();
                    userStatuses.add(existingUserStatus);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            // 파일이 없는 경우, 새 파일 생성 후 저장
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 사용자 상태 업데이트 또는 추가
        boolean userStatusUpdated = false;
        for (int i = 0; i < userStatuses.size(); i++) {
            if (userStatuses.get(i).getUserId().equals(userStatus.getUserId())) {
                userStatuses.set(i, userStatus);
                userStatusUpdated = true;
                break;
            }
        }
        if (!userStatusUpdated) {
            userStatuses.add(userStatus);
        }

        // 파일에 다시 쓰기
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORY.toFile()))) {
            for (UserStatus us : userStatuses) {
                oos.writeObject(us);
            }
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