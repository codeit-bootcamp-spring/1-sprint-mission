package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;


@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository extends FileRepository implements UserRepository {

    public FileUserRepository(@Value("${discodeit.repository.user}")String fileDirectory) {
        super(fileDirectory);
    }
    @Override
    public void save(User user) {
        Path path = resolvePath(user.getId());
        saveToFile(path,user);
    }

    @Override
    public Optional<User> findById(UUID userId) {
       Path path = resolvePath(userId);
        return loadFromFile(path);
    }

    @Override
    public Map<UUID, User> findAll() {
        Map<UUID,User> userMap = new HashMap<>();
        try (Stream<Path> pathStream = Files.walk(getDIRECTORY())){
            pathStream.filter(path -> path.toString().endsWith(".ser")).forEach(path -> {
                Optional<User> user = loadFromFile(path);
                user.ifPresent(us -> userMap.put(us.getId(), us));
            });
        } catch (IOException e) {
            System.out.println("파일을 읽을 수 없습니다." + e.getMessage());
        }
        return userMap;
    }

    @Override
    public void delete(UUID userId) {
        Path path = resolvePath(userId);
        deleteFile(path);
    }

    @Override
    public boolean existsById(UUID userId) {
        Path path = resolvePath(userId);
        return Files.exists(path);
    }

/*    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        try (Stream<Path> pathStream = Files.walk(getDIRECTORY())) {
            return pathStream.filter(path -> path.toString().endsWith(".ser"))
                    .map(this::loadFromFile)  // loadFromFile 메서드로 파일에서 User 객체를 로드
                    .filter(Optional::isPresent)  // Optional이 비어 있지 않으면
                    .map(Optional::get)  // Optional에서 실제 User 객체를 가져옴
                    .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))  // username과 password가 일치하는지 확인
                    .findFirst();  // 일치하는 첫 번째 User 객체를 반환
        } catch (IOException e) {
            System.out.println("파일을 읽을 수 없습니다. " + e.getMessage());
            return Optional.empty();  // 예외 발생 시 빈 Optional 반환
        }
    }*/

}