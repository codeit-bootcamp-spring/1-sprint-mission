package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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
            oos.writeObject(user);
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