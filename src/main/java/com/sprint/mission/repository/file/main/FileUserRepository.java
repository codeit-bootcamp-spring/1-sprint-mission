package com.sprint.mission.repository.file.main;

import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FileUserRepository implements UserRepository {

    // User 디렉토리 Path 객체
    private static final Path USER_DIRECT_PATH = Path.of("userDirectory");

    //@Override
    @SneakyThrows
    public void save(User user) {
        Path filePath = USER_DIRECT_PATH.resolve(user.getId() + ".ser");

        // 파일 존재하지 않으면(나중에 수정할 때 saveUser메서드 활용해야 하므로 구분 필요할것 같음
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath));
        oos.writeObject(user);
        oos.close();
    }

    /**
     * 조회
     */

    @SneakyThrows
    @Override
    public Optional<User> findById(UUID userId) {
        // 1. id로 filePath 얻기
        Path userFilePath = getUserFilePath(userId);

        // 2. 1에서 얻은 filePath가 유효한지 테스트
        if (!Files.exists(userFilePath)) {
            throw new NotFoundId();
        }

        // 3. 읽기
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(userFilePath))) {
            return Optional.ofNullable(ois.readObject())
                    .map(obj -> (User) obj);
        }
    }

    @SneakyThrows
    @Override
    public List<User> findAll() {
        return Files.exists(USER_DIRECT_PATH)
                ? Files.list(USER_DIRECT_PATH)
                .filter(path -> path.toString().endsWith(".ser"))
                .map(this::readUserFromFile)
                .collect(Collectors.toCollection(ArrayList::new))
                : new ArrayList<>();
    }

    @SneakyThrows
    @Override
    public void delete(UUID userId) {
        Files.delete(getUserFilePath(userId));
    }

    @SneakyThrows
    @Override
    public boolean existsById(UUID userId) {
        return Files.exists(getUserFilePath(userId));
    }

    /**
     * 편의 메서드 모음
     */
    private User readUserFromFile(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 역직렬화 실패: " + e.getMessage());
        }
    }

    // Path 만드는 역할만 (검증은 x)
    private Path getUserFilePath(UUID userId) {
        return USER_DIRECT_PATH.resolve(userId.toString() + ".ser");
    }


    // 테스트 시 파일 디렉토리 세팅을 위한 메서드
    public void createUserDirectory() throws IOException {
        // 존재하면 안의 파일 전부 삭제
        if (Files.exists(USER_DIRECT_PATH)) {
            try {
                Files.list(USER_DIRECT_PATH).forEach(path -> {
                    try {
                        Files.delete(path); // 개별 파일 삭제
                    } catch (IOException e) {
                        throw new RuntimeException("파일 삭제 실패" + e.getMessage());
                    }
                });
            } catch (IOException e) {
                System.out.println("U 디렉토리 초기화 실패");
            }
        } else {
            Files.createDirectory(USER_DIRECT_PATH);
        }
    }
}

