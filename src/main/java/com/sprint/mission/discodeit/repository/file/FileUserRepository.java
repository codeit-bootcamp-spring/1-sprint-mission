package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class FileUserRepository implements UserRepository, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 변경 가능성 없으므로 객체 생성 시 멤버 변수로 초기화
    private final Path userDirectory = Paths.get(System.getProperty("user.dir"), "/data/user");

    public FileUserRepository() {
        init();
    }

    // 파일 저장
    @Override
    public void save(User user) {

        Path filePath = returnFilePath(user.getId());

        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 디렉토리 안 모든 파일 가져오기
    @Override
    public Map<UUID, User> load() {

        if (Files.exists(userDirectory)) {
            try {
                // 파일 목록 각각에서 파일 역직렬화
                Map<UUID, User> map = Files.list(userDirectory)    // 해당 디렉터리 파일 목록 가져옴
                        .map(filePath -> {
                            try (
                                    FileInputStream fis = new FileInputStream(filePath.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis);
                            ) {
                                User user = (User) ois.readObject();
                                return Map.entry(user.getId(), user);
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                return map;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new HashMap<>();
        }
    }

    // 파일 삭제
    @Override
    public void delete(UUID id) {

        Path filePath = returnFilePath(id);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (NoSuchFileException e) {
            System.out.println("존재하지 않는 유저입니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일 경로 초기화
    private void init() {

        if (!Files.exists(userDirectory)) {
            try {
                Files.createDirectories(userDirectory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path returnFilePath(UUID id) {

        return userDirectory.resolve(id.toString().concat(".ser"));
    }
}
