package com.sprint.mission.discodeit.service.file;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileUserService implements UserService {

    private final HashMap<UUID, User> data = new HashMap<>();
    private final UserValidator userValidator;
    private static final Logger LOGGER = Logger.getLogger(FileUserRepository.class.getName());
    private final Path directory = Paths.get(System.getProperty("user.dir"), "Data/user_data");
    private final String fileName = "user_data.ser";

    public FileUserService(UserValidator userValidator) {
        this.userValidator = userValidator;
        init(directory);
        loadDataFromFile();
    }


    @Override
    public User createUser(String name, String email,String iD ,String password) {
        try{
            userValidator.validateUser(name, email, password);
        }catch (CustomException e){
            System.out.println("유저생성 실패 ->" + e.getMessage());
        }
        User user = new User(name, email, iD, password);
        data.put(user.getId(), user);
        saveDataToFile();
        return user;
    }


    @Override
    public User getUser(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public HashMap<UUID, User> getAllUsers() {
        return new HashMap<>(data);
    }

    @Override
    public void updateUser(UUID uuid, String email, String id, String password) {
        User user = getUser(uuid);
        if (user != null) {
            user.update(email, id, password);
        }
        saveDataToFile();
    }

    @Override
    public void deleteUser(UUID uuid) {
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
