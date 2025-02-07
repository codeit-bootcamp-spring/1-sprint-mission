package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    //<UserStatus id, UserStatus>
    private final HashMap<UUID, UserStatus> userStatusStore = new HashMap<>();
    //<User id, UserStatus id>
    private final HashMap<UUID, UUID> userToStatusMap = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(FileUserStatusRepository.class.getName());
    private final Path directory = Paths.get(System.getProperty("user.dir"), "Data/userStatus_data");
    private final String userStatusStoreFileName = "data.ser";
    private final String userToStatusMapFileName = "userMappingTable.ser";

    public FileUserStatusRepository(){
        init(directory);
        loadDataFromFile(userStatusStoreFileName, userStatusStore);
        loadDataFromFile(userToStatusMapFileName, userToStatusMap);
    }

    @Override
    public void save(UserStatus userStatus) {
        userStatusStore.put(userStatus.getId(), userStatus);
        userToStatusMap.put(userStatus.getUserId(), userStatus.getId());
        saveDataToFile(userStatusStoreFileName, userStatusStore);
        saveDataToFile(userToStatusMapFileName, userToStatusMap);

    }

    @Override
    public UserStatus findById(UUID id) {
        return userStatusStore.get(id);
    }

    @Override
    public UserStatus findByUserId(UUID userId){
        UUID id= userToStatusMap.get(userId);
        return userStatusStore.get(id);
    }

    @Override
    public HashMap<UUID, UserStatus> findAll() {
        return new HashMap<>(userStatusStore);
    }

    @Override
    public void delete(UUID id) {
        userStatusStore.remove(id);
        saveDataToFile(userStatusStoreFileName, userStatusStore);
        saveDataToFile(userToStatusMapFileName, userToStatusMap);
    }

    private void init(Path directory){
        if(!Files.exists(directory)){
            try {
                Files.createDirectories(directory);
            } catch (IOException e){
                throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage());
            }
        }
    }

    private <K, V> void saveDataToFile(String fileName, Map<K, V> data) {
        Path filePath = directory.resolve(fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(data);  // Map<UUID, User>만 직렬화하여 저장
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage());
        }
    }

    private <K, V> void loadDataFromFile(String fileName,Map<K, V> data) {
        Path filePath = directory.resolve(fileName);
        if (Files.exists(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                // Unchecked cast 경고를 피하기 위해 Map<UUID, User>로 안전하게 캐스팅
                Object readObject = ois.readObject();
                if (readObject instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<K, V> loadedData = (Map<K, V>) readObject;
                    data.putAll(loadedData);  // 직렬화된 Map<UUID, User>을 로드
                } else {
                    throw new RuntimeException("잘못된 데이터 형식");
                }
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.WARNING, "파일 로드 실패", e);
            }
        }
    }
}
