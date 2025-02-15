package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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
public class FileUserStatusRepository extends FileRepository implements UserStatusRepository {

    public FileUserStatusRepository(@Value("${discodeit.repository.userStatus}")String fileDirectory) {
        super(fileDirectory);
    }

    @Override
    public void save(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getId());
        saveToFile(path,userStatus);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        Path path = resolvePath(id);
        return loadFromFile(path);
    }

    @Override
    public Map<UUID, UserStatus> findAll() {
        Map<UUID, UserStatus> userStatusMap = new HashMap<>();
        try (Stream<Path> pathStream = Files.walk(getDIRECTORY())){
            pathStream.filter(path -> path.toString().endsWith(".ser"))
                    .forEach(path -> {
                        Optional<UserStatus> userStatus = loadFromFile(path);
                        userStatus.ifPresent(us -> userStatusMap.put(us.getId(), us));
                    });
        } catch (IOException e) {
            System.out.println("파일을 읽을 수 없습니다." + e.getMessage());
        }
        return userStatusMap;
    }

    @Override
    public void delete(UUID id) {
        Path path = resolvePath(id);
        deleteFile(path);
    }
}
