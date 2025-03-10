package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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
public class FileUserRepository implements UserRepository {

    private final Path DIRECTORY;

    public FileUserRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "User.ser");
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createFile(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public User save(User user) {
        List<User> users = new ArrayList<>();
        try {
            if (Files.exists(DIRECTORY) && Files.size(DIRECTORY) > 0) { // 파일이 존재하고 내용이 있는 경우에만 읽기
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORY.toFile()))) {
                    while (true) {
                        try {
                            User existingUser = (User) ois.readObject();
                            users.add(existingUser);
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

        boolean userUpdated = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                userUpdated = true;
                break;
            }
        }
        if (!userUpdated) {
            users.add(user);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORY.toFile()))) {
            for (User u : users) {
                oos.writeObject(u);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
    @Override
    public Optional<User> findById(UUID id) {
        List<User> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.readAllContents().stream()
                .filter(user -> user.getAlias().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAllByIdIn(List<UUID> ids) {
        List<User> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }
    @Override
    public List<User> readAllContents() {
        List<User> contents = new ArrayList<>();
        if (Files.exists(DIRECTORY)) {
            try (FileInputStream fis = new FileInputStream(DIRECTORY.toFile())) {
                try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                    while (true) {
                        try {
                            User content = (User) ois.readObject();
                            contents.add(content);
                        } catch (EOFException e) {
                            break;
                        }
                    }
                } catch (EOFException e) {
                    return contents;
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
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
    public boolean existsByEmail(String email) {
        return this.readAllContents().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.readAllContents().stream()
                .anyMatch(user -> user.getAlias().equals(username));
    }


    @Override
    public void deleteById(UUID id) {
        List<User> allContents = readAllContents();
        List<User> updatedUsers = allContents.stream()
                .filter(user -> !user.getId().equals(id))
                .toList();
        saveAllContents(updatedUsers);
    }

    private void saveAllContents(List<User> users) {
        try (
                FileOutputStream fos = new FileOutputStream(DIRECTORY.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            for (User user : users) {
                oos.writeObject(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
