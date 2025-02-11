package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileUserRepository implements UserRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserRepository(){
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "data", User.class.getSimpleName());
        if (!Files.exists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
                System.out.println("디렉토리 생성 완료");
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    @Override
    public User save(User user) {
        Path path = resolvePath(user.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        User userNullable = null;
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                userNullable = (User) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return Optional.ofNullable(userNullable);
    }

    @Override
    public List<User> findAll() {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            return paths.filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            return (User) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean existsId(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);
    }

    @Override
    public void delete(UUID id) {
        Path path = resolvePath(id);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
