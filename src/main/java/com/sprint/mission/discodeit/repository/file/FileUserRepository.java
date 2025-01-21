package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUserRepository implements UserRepository {

    private final HashMap<UUID, User> data = new HashMap<>();

    private static final Logger LOGGER = Logger.getLogger(FileUserRepository.class.getName());
    private final Path directory = Paths.get(System.getProperty("user.dir"), "Data/user_data");
    private final String fileName = "user_data.ser";


    public FileUserRepository(){
        init(directory);  // 파일 디렉토리 초기화
        loadDataFromFile();// 데이터 파일에서 불러오기
    }


    public void save(User user) {
        data.put(user.getuuID(), user);
        saveDataToFile();
    }

    @Override
    public User findById(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public HashMap<UUID, User> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public void delete(UUID uuid) {
        data.remove(uuid);
        saveDataToFile();
    }

    // 디렉토리 초기화
    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage());
            }
        }
    }

    //전체 HashMap을 직렬화하여 파일에 저장
    private void saveDataToFile() {
        Path filePath = directory.resolve(fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(data);  // Map<UUID, User>만 직렬화하여 저장
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage());
        }
    }

    // 파일에서 직렬화된 객체를 불러오기
    private void loadDataFromFile() {
        Path filePath = directory.resolve(fileName);
        if (Files.exists(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                // Unchecked cast 경고를 피하기 위해 Map<UUID, User>로 안전하게 캐스팅
                Object readObject = ois.readObject();
                if (readObject instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<UUID, User> loadedData = (Map<UUID, User>) readObject;
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
